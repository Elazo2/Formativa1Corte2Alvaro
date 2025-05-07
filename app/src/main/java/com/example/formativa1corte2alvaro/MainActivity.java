package com.example.formativa1corte2alvaro;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText TxtX1, TxtX2, TxtY1, TxtY2;
    Button btnPendiente, btnPuntoMedio, btnEcuaLineal, btnCuadrantes;
    private LinearLayout mainLayout;
    TextView txtResultado;
    Random random = new Random();
    View currentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TxtX1 = findViewById(R.id.TxtX1);
        TxtY1 = findViewById(R.id.TxtY1);
        TxtX2 = findViewById(R.id.TxtX2);
        TxtY2 = findViewById(R.id.TxtY2);
        txtResultado = findViewById(R.id.txtResultado);

        btnPendiente = findViewById(R.id.btnPendiente);
        btnPuntoMedio = findViewById(R.id.btnPuntoMedio);
        btnEcuaLineal = findViewById(R.id.btnEcuaLineal);
        btnCuadrantes = findViewById(R.id.btnCuadrantes);
        mainLayout = findViewById(R.id.main);

        registerForContextMenu(mainLayout);
        registerForContextMenu(TxtX1);
        registerForContextMenu(TxtY1);
        registerForContextMenu(TxtX2);
        registerForContextMenu(TxtY2);

        btnPendiente.setOnClickListener(v -> {
            if (!validarEntradas()) return;

            double x1 = getValor(TxtX1);
            double y1 = getValor(TxtY1);
            double x2 = getValor(TxtX2);
            double y2 = getValor(TxtY2);

            if (x2 == x1) {
                txtResultado.setText("Pendiente indefinida (recta vertical)");
            } else {
                double pendiente = (y2 - y1) / (x2 - x1);
                txtResultado.setText(String.format(Locale.US, "Pendiente: %.2f", pendiente));
            }
        });

        btnPuntoMedio.setOnClickListener(v -> {
            if (!validarEntradas()) return;

            double x1 = getValor(TxtX1);
            double y1 = getValor(TxtY1);
            double x2 = getValor(TxtX2);
            double y2 = getValor(TxtY2);

            double xm = (x1 + x2) / 2;
            double ym = (y1 + y2) / 2;

            txtResultado.setText(String.format(Locale.US, "Punto medio: (%.2f, %.2f)", xm, ym));
        });

        btnEcuaLineal.setOnClickListener(v -> {
            if (!validarEntradas()) return;

            double x1 = getValor(TxtX1);
            double y1 = getValor(TxtY1);
            double x2 = getValor(TxtX2);
            double y2 = getValor(TxtY2);

            if (x2 == x1) {
                txtResultado.setText(String.format(Locale.US, "Recta vertical: x = %.2f", x1));
            } else {
                double m = (y2 - y1) / (x2 - x1);
                double b = y1 - m * x1;
                String ecuacion = String.format(Locale.US, "Ecuación: y = %.2fx %s %.2f", m, b >= 0 ? "+" : "-", Math.abs(b));
                txtResultado.setText(ecuacion);
            }
        });

        btnCuadrantes.setOnClickListener(v -> {
            if (!validarEntradas()) return;

            double x1 = getValor(TxtX1);
            double y1 = getValor(TxtY1);
            double x2 = getValor(TxtX2);
            double y2 = getValor(TxtY2);

            String cuadrante1 = obtenerCuadrante(x1, y1);
            String cuadrante2 = obtenerCuadrante(x2, y2);

            txtResultado.setText("Punto 1: " + cuadrante1 + "\nPunto 2: " + cuadrante2);
        });
    }

    private boolean validarEntradas() {
        try {
            getValor(TxtX1);
            getValor(TxtY1);
            getValor(TxtX2);
            getValor(TxtY2);
            return true;
        } catch (NumberFormatException e) {
            txtResultado.setText("Verifica los datos. Solo se permiten números.");
            return false;
        }
    }

    private double getValor(EditText editText) throws NumberFormatException {
        String texto = editText.getText().toString().trim();
        return Double.parseDouble(texto);
    }

    private String obtenerCuadrante(double x, double y) {
        if (x > 0 && y > 0) return "Primer cuadrante";
        else if (x < 0 && y > 0) return "Segundo cuadrante";
        else if (x < 0 && y < 0) return "Tercer cuadrante";
        else if (x > 0 && y < 0) return "Cuarto cuadrante";
        else if (x == 0 && y == 0) return "Origen";
        else if (x == 0) return "Sobre el eje Y";
        else return "Sobre el eje X";
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        currentEditText = v;

        menu.setHeaderTitle("Opciones");

        if (v instanceof EditText) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_editext, menu);
        } else if (v.getId() == R.id.main) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        View focusedView = getCurrentFocus();

        if (item.getItemId() == R.id.menu_aleatorio) {
            if (focusedView instanceof EditText) {
                EditText editText = (EditText) focusedView;
                editText.setText(String.valueOf(random.nextInt(90)  + 1));
            }
            return true;

        } else if (item.getItemId() == R.id.menu_cambiar_signo) {
            if (focusedView instanceof EditText) {
                EditText editText = (EditText) focusedView;
                try {
                    float valor = Float.parseFloat(editText.getText().toString());
                    editText.setText(String.valueOf(-valor));
                } catch (Exception ignored) {
                }
            }
            return true;

        } else if (item.getItemId() == R.id.genPuntosPrimer) {
            generarPuntosEnCuadrante(1);
            return true;

        } else if (item.getItemId() == R.id.genPuntosSegundo) {
            generarPuntosEnCuadrante(2);
            return true;

        } else if (item.getItemId() == R.id.genPuntosTercero) {
            generarPuntosEnCuadrante(3);
            return true;

        } else if (item.getItemId() == R.id.genPuntosCuarto) {
            generarPuntosEnCuadrante(4);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void generarPuntosEnCuadrante(int cuadrante) {
        double x = 0, y = 0;

        switch (cuadrante) {
            case 1: x = Math.random() * 100; y = Math.random() * 100; break;
            case 2: x = -Math.random() * 100; y = Math.random() * 100; break;
            case 3: x = -Math.random() * 100; y = -Math.random() * 100; break;
            case 4: x = Math.random() * 100; y = -Math.random() * 100; break;
        }

        TxtX1.setText(String.format(Locale.US, "%.2f", x));
        TxtY1.setText(String.format(Locale.US, "%.2f", y));
        TxtX2.setText(String.format(Locale.US, "%.2f", x + 10));
        TxtY2.setText(String.format(Locale.US, "%.2f", y + 10));

        txtResultado.setText("Puntos generados en cuadrante " + cuadrante);
    }
}
