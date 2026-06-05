package com.luis.myactivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_FULL_NAMES = "fullNames";
    public static final String KEY_BIRTHDATE = "birthdate";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_DESCRIPTION = "description";

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

        Button btnNext = findViewById(R.id.btnNext);
        Button btnCancelDate = findViewById(R.id.btnCancelDate);
        Button btnOKDate = findViewById(R.id.btnOKDate);
        DatePicker viewDatePicker = getDatePicker();
        btnOKDate.setOnClickListener(view -> {
            btnOKDate.setEnabled(false);
            viewDatePicker.setEnabled(false);
        });
        btnCancelDate.setOnClickListener(view -> {
            btnOKDate.setEnabled(true);
            viewDatePicker.setEnabled(true);
        });
        btnNext.setOnClickListener(view -> goToConfirmActivity());
        loadPreviousContact();
    }

    private void loadPreviousContact() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String fullNames = bundle.getString(KEY_FULL_NAMES);
            String birthdate = bundle.getString(KEY_BIRTHDATE);
            String phone = bundle.getString(KEY_PHONE);
            String email = bundle.getString(KEY_EMAIL);
            String description = bundle.getString(KEY_DESCRIPTION);

            setTextFromTextInputLayoutResId(R.id.tlFullNames, fullNames);
            setTextFromTextInputLayoutResId(R.id.tlPhone, phone);
            setTextFromTextInputLayoutResId(R.id.tlEmail, email);
            setTextFromTextInputLayoutResId(R.id.tlContactDesc, description);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(birthdate, formatter);
            int day = localDate.getDayOfMonth();
            int month = localDate.getMonthValue() - 1;
            int year = localDate.getYear();
            DatePicker viewDatePicker = getDatePicker();
            viewDatePicker.init(year, month, day, null);
        }
    }

    private void setTextFromTextInputLayoutResId(int resId, String value) {
        TextInputLayout textInputLayout = findViewById(resId);
        if (textInputLayout.getEditText() != null) textInputLayout.getEditText().setText(value);
    }

    private Contact getInfo() {
        String fullName = getStringFromTextInputLayoutResID(R.id.tlFullNames);
        String birthdate = getBirthdate();
        String phone = getStringFromTextInputLayoutResID(R.id.tlPhone);
        String email = getStringFromTextInputLayoutResID(R.id.tlEmail);
        String description = getStringFromTextInputLayoutResID(R.id.tlContactDesc);
        return new Contact(fullName, birthdate, phone, email, description);
    }

    private String getStringFromTextInputLayoutResID(int resID) {
        TextInputLayout textInputLayout = findViewById(resID);
        EditText editText = textInputLayout.getEditText();
        return (editText != null) ? textInputLayout.getEditText().getText().toString() : "";
    }

    private String getBirthdate() {
        DatePicker viewDatePicker = getDatePicker();
        int day = viewDatePicker.getDayOfMonth();
        int month = viewDatePicker.getMonth() + 1;
        int year = viewDatePicker.getYear();
        return String.format(Locale.US, "%02d/%02d/%04d", day, month, year);
    }

    private void goToConfirmActivity() {
        Contact contact = getInfo();
        checkFields(contact, () -> {
            Intent intent = new Intent(getBaseContext(), ConfirmationActivity.class);
            intent.putExtra(KEY_FULL_NAMES, contact.getFullNames());
            intent.putExtra(KEY_BIRTHDATE, contact.getBirthdate());
            intent.putExtra(KEY_PHONE, contact.getPhone());
            intent.putExtra(KEY_EMAIL, contact.getEmail());
            intent.putExtra(KEY_DESCRIPTION, contact.getDescription());
            startActivity(intent);
            finish();
        }, fields -> {
            String msg = getString(R.string.msg_invalid_fields).concat(":\n").concat(fields);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

    }

    private void checkFields(Contact contact, Runnable onSuccess, Consumer<String> failure) {
        StringBuilder invalidFields = new StringBuilder();
        DatePicker viewDatePicker = getDatePicker();
        boolean isValidFullName = contact.getFullNames() != null && !contact.getFullNames().isBlank();
        boolean isValidDate = !viewDatePicker.isEnabled();
        boolean isValidPhone = contact.getPhone() != null && !contact.getPhone().isBlank();
        boolean isValidEmail = Patterns.EMAIL_ADDRESS.matcher(contact.getEmail().trim()).matches();
        boolean isValidDescription = contact.getDescription() != null && !contact.getDescription().isBlank();
        if (!isValidFullName) appendInvalidField(invalidFields, R.string.label_full_name);
        if (!isValidDate) appendInvalidField(invalidFields, R.string.label_birthdate);
        if (!isValidPhone) appendInvalidField(invalidFields, R.string.label_phone);
        if (!isValidEmail) appendInvalidField(invalidFields, R.string.label_email);
        if (!isValidDescription) appendInvalidField(invalidFields, R.string.label_description);
        if (isValidFullName && isValidDate && isValidPhone && isValidEmail && isValidDescription)
            onSuccess.run();
        else failure.accept(invalidFields.toString());
    }

    private void appendInvalidField(StringBuilder sb, int stringResId) {
        if (sb.length() > 0) sb.append("\n");
        sb.append(getString(stringResId));
    }

    private DatePicker getDatePicker() {
        return findViewById(R.id.viewDatePicker);
    }

}