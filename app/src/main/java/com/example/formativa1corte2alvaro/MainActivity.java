package com.example.formativa1corte2alvaro;

import android.os.Bundle;
import android.view.ContextMenu;
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

public class MainActivity extends AppCompatActivity {

    EditText TxtX1, TxtX2, TxtY1, TxtY2;
    Button btnPendiente, btnPuntoMedio, btnEcuaLineal, btnCuadrantes;
    LinearLayout mainLayout;
    TextView txtResultado;


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

        btnPendiente.setOnClickListener(v -> {
            try {
                double x1 = Double.parseDouble(TxtX1.getText().toString());
                double y1 = Double.parseDouble(TxtY1.getText().toString());
                double x2 = Double.parseDouble(TxtX2.getText().toString());
                double y2 = Double.parseDouble(TxtY2.getText().toString());

                if (x2 - x1 == 0) {
                    txtResultado.setText("Pendiente indefinida (vertical)");
                } else {
                    double pendiente = (y2 - y1) / (x2 - x1);
                    txtResultado.setText("Pendiente: " + pendiente);
                }
            } catch (Exception e) {
                txtResultado.setText("Verifica los datos");
            }
        });


        btnPuntoMedio.setOnClickListener(v -> {
            try {
                double x1 = Double.parseDouble(TxtX1.getText().toString());
                double y1 = Double.parseDouble(TxtY1.getText().toString());
                double x2 = Double.parseDouble(TxtX2.getText().toString());
                double y2 = Double.parseDouble(TxtY2.getText().toString());

                double xm = (x1 + x2) / 2;
                double ym = (y1 + y2) / 2;

                txtResultado.setText("Punto medio: (" + xm + ", " + ym + ")");
            } catch (Exception e) {
                txtResultado.setText("Verifica los datos");
            }
        });

        btnEcuaLineal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double x1 = Double.parseDouble(TxtX1.getText().toString());
                    double y1 = Double.parseDouble(TxtY1.getText().toString());
                    double x2 = Double.parseDouble(TxtX2.getText().toString());
                    double y2 = Double.parseDouble(TxtY2.getText().toString());

                    if (x2 - x1 == 0) {
                        txtResultado.setText("Recta vertical: x = " + x1);
                    } else {
                        double m = (y2 - y1) / (x2 - x1);
                        double b = y1 - m * x1;

                        String ecuacion;
                        if (b >= 0) {
                            ecuacion = "Ecuación: y = " + String.format("%.2f", m) + "x + " + String.format("%.2f", b);
                        } else {
                            ecuacion = "Ecuación: y = " + String.format("%.2f", m) + "x - " + String.format("%.2f", Math.abs(b));
                        }

                        txtResultado.setText(ecuacion);
                    }
                } catch (Exception e) {
                    txtResultado.setText("Verifica los datos");
                }
            }
        });

        btnCuadrantes.setOnClickListener(v -> {
            try {
                double x1 = Double.parseDouble(TxtX1.getText().toString());
                double y1 = Double.parseDouble(TxtY1.getText().toString());
                double x2 = Double.parseDouble(TxtX2.getText().toString());
                double y2 = Double.parseDouble(TxtY2.getText().toString());

                String cuadrante1 = obtenerCuadrante(x1, y1);
                String cuadrante2 = obtenerCuadrante(x2, y2);

                txtResultado.setText("Punto 1: " + cuadrante1 + "\nPunto 2: " + cuadrante2);

            } catch (Exception e) {
                txtResultado.setText("Verifica los datos");
            }
        });
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
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.genPuntosPrimer) {
            generarPuntosEnCuadrante(1);
            return true;
        } else if (id == R.id.genPuntosSegundo) {
            generarPuntosEnCuadrante(2);
            return true;
        } else if (id == R.id.genPuntosTercero) {
            generarPuntosEnCuadrante(3);
            return true;
        } else if (id == R.id.genPuntosCuarto) {
            generarPuntosEnCuadrante(4);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void generarPuntosEnCuadrante(int cuadrante) {
        double x = 0, y = 0;
        switch (cuadrante) {
            case 1:
                x = Math.random() * 100 + 1;
                y = Math.random() * 100 + 1;
                break;
            case 2:
                x = -1 * (Math.random() * 100 + 1);
                y = Math.random() * 100 + 1;
                break;
            case 3:
                x = -1 * (Math.random() * 100 + 1);
                y = -1 * (Math.random() * 100 + 1);
                break;
            case 4:
                x = Math.random() * 100 + 1;
                y = -1 * (Math.random() * 100 + 1);
                break;
        }
        TxtX1.setText(String.format("%.2f", x));
        TxtY1.setText(String.format("%.2f", y));
        TxtX2.setText(String.format("%.2f", x + 10));
        TxtY2.setText(String.format("%.2f", y + 10));

        txtResultado.setText("Puntos generados en cuadrante " + cuadrante);

    }
}
