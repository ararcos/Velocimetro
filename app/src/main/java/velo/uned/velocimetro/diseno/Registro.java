package velo.uned.velocimetro.diseno;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import velo.uned.velocimetro.databinding.ActivityRegistroBinding;
import velo.uned.velocimetro.modelo.Users;
import velo.uned.velocimetro.servicios.UsersServicio;
import velo.uned.velocimetro.R;

public class Registro extends AppCompatActivity {
    Users users;
    UsersServicio usersServicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegistroBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_registro);
        users=new Users();
        usersServicio=new UsersServicio(this );
        binding.setDbuser(users);
    }

    public void registrar(View view) {
        if (usersServicio.addUser(users)) {
                Toast.makeText(this, "Guardado Correctamente!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ocurrio Un error al guardar!", Toast.LENGTH_SHORT).show();
        }

    }
}
