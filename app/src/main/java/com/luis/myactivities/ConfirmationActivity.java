package com.luis.myactivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmation_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.confirmation), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtNames = findViewById(R.id.txtNames);
        TextView txtBirthdate = findViewById(R.id.txtBirthdate);
        TextView txtPhone = findViewById(R.id.txtPhone);
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView txtDescription = findViewById(R.id.txtDescription);
        Contact contact = getInfo();
        txtNames.setText(contact.getFullNames());
        txtBirthdate.setText(getString(R.string.label_birthdate).concat(": ").concat(contact.getBirthdate()));
        txtPhone.setText(getString(R.string.label_phone).concat(": ").concat(contact.getPhone()));
        txtEmail.setText(getString(R.string.label_email).concat(": ").concat(contact.getEmail()));
        txtDescription.setText(getString(R.string.label_description).concat(": ").concat(contact.getDescription()));

        Button btnEditData = findViewById(R.id.btnEditData);
        btnEditData.setOnClickListener(view -> goToMainActivity(contact));
    }

    private void goToMainActivity(Contact contact) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra(MainActivity.KEY_FULL_NAMES, contact.getFullNames());
        intent.putExtra(MainActivity.KEY_BIRTHDATE, contact.getBirthdate());
        intent.putExtra(MainActivity.KEY_PHONE, contact.getPhone());
        intent.putExtra(MainActivity.KEY_EMAIL, contact.getEmail());
        intent.putExtra(MainActivity.KEY_DESCRIPTION, contact.getDescription());
        startActivity(intent);
        finish();
    }

    private Contact getInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String fullNames = (bundle != null) ? bundle.getString(MainActivity.KEY_FULL_NAMES) : "";
        String birthdate = (bundle != null) ? bundle.getString(MainActivity.KEY_BIRTHDATE) : "";
        String phone = (bundle != null) ? bundle.getString(MainActivity.KEY_PHONE) : "";
        String email = (bundle != null) ? bundle.getString(MainActivity.KEY_EMAIL) : "";
        String description = (bundle != null) ? bundle.getString(MainActivity.KEY_DESCRIPTION) : "";
        return new Contact(fullNames, birthdate, phone, email, description);
    }

}
