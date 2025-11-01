package com.example.assignment3_sqldatabase;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText nationalNumber, name, species, height, weight, hp, attack, defense;
    private Spinner levelSpinner;
    private RadioGroup radioGroup;

    private TextView nameCol, speciesCol, heightCol, weightCol,
            hpCol, attackCol, defenseCol, genderCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setupSpinner();
        setButtonListeners();
    }

    private void bindViews() {
        nationalNumber = findViewById(R.id.nationalNumber);
        name = findViewById(R.id.name);
        species = findViewById(R.id.species);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        hp = findViewById(R.id.hp);
        attack = findViewById(R.id.attack);
        defense = findViewById(R.id.defense);
        levelSpinner = findViewById(R.id.levelSpinner);
        radioGroup = findViewById(R.id.genderGroup);

        nameCol = findViewById(R.id.nameLabel);
        speciesCol = findViewById(R.id.speciesLabel);
        heightCol = findViewById(R.id.heightLabel);
        weightCol = findViewById(R.id.weightLabel);
        hpCol = findViewById(R.id.hpLabel);
        attackCol = findViewById(R.id.attackLabel);
        defenseCol = findViewById(R.id.defenseLabel);
        genderCol = findViewById(R.id.genderLabel);
    }

    private void setupSpinner() {
        ArrayList<String> levels = new ArrayList<>();
        for (int i = 1; i <= 50; i++) levels.add(String.valueOf(i));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(adapter);
    }

    private void setButtonListeners() {
        Button resetButton = findViewById(R.id.resetButton);
        Button saveButton = findViewById(R.id.saveButton);

        resetButton.setOnClickListener(v -> resetAllFields());
        saveButton.setOnClickListener(v -> submit());
    }

    private void resetAllFields() {
        nationalNumber.setText("");
        name.setText("");
        species.setText("");
        height.setText("");
        weight.setText("");
        hp.setText("");
        attack.setText("");
        defense.setText("");
        radioGroup.clearCheck();
        levelSpinner.setSelection(0);
        resetColors();
    }

    private void resetColors() {
        int defaultColor = getResources().getColor(android.R.color.black);
        nameCol.setTextColor(defaultColor);
        speciesCol.setTextColor(defaultColor);
        heightCol.setTextColor(defaultColor);
        weightCol.setTextColor(defaultColor);
        genderCol.setTextColor(defaultColor);
        hpCol.setTextColor(defaultColor);
        attackCol.setTextColor(defaultColor);
        defenseCol.setTextColor(defaultColor);
    }

    private void submit() {
        resetColors();
        boolean allValid = true;
        StringBuilder errors = new StringBuilder();

        if (nationalNumber.getText().toString().trim().isEmpty() ||
                name.getText().toString().trim().isEmpty() ||
                species.getText().toString().trim().isEmpty() ||
                height.getText().toString().trim().isEmpty() ||
                weight.getText().toString().trim().isEmpty() ||
                hp.getText().toString().trim().isEmpty() ||
                attack.getText().toString().trim().isEmpty() ||
                defense.getText().toString().trim().isEmpty()) {
            errors.append("All fields must be filled!\n");
            allValid = false;
        }

        int natNum = 0;
        try {
            natNum = Integer.parseInt(nationalNumber.getText().toString());
            if (natNum < 0 || natNum > 1010) {
                errors.append("National Number must be 0–1010\n");
                allValid = false;
            }
        } catch (NumberFormatException e) {
            errors.append("Invalid National Number\n");
            allValid = false;
        }

        String nameVal = name.getText().toString().trim();
        if (!nameVal.matches("[A-Za-z. ]{3,12}")) {
            nameCol.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            errors.append("Name must be 3–12 letters\n");
            allValid = false;
        }

        String speciesVal = species.getText().toString().trim();
        if (!speciesVal.matches("[A-Za-z ]+")) {
            speciesCol.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            errors.append("Species may contain only letters and spaces\n");
            allValid = false;
        }

        int selectedGenderId = radioGroup.getCheckedRadioButtonId();
        String genderVal = "";
        if (selectedGenderId == -1) {
            genderCol.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            errors.append("Gender must be selected\n");
            allValid = false;
        } else {
            RadioButton rb = findViewById(selectedGenderId);
            genderVal = rb.getText().toString();
        }

        double hVal = 0, wVal = 0;
        int hpVal = 0, atkVal = 0, defVal = 0, levelVal = 0;

        try {
            hVal = Double.parseDouble(height.getText().toString());
            if (hVal < 0.2 || hVal > 169.99) {
                heightCol.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                errors.append("Height must be 0.2–169.99 m\n");
                allValid = false;
            }
        } catch (NumberFormatException e) {
            errors.append("Invalid Height\n");
            allValid = false;
        }

        try {
            wVal = Double.parseDouble(weight.getText().toString());
            if (wVal < 0.1 || wVal > 992.7) {
                weightCol.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                errors.append("Weight must be 0.1–992.7 kg\n");
                allValid = false;
            }
        } catch (NumberFormatException e) {
            errors.append("Invalid Weight\n");
            allValid = false;
        }

        try { hpVal = Integer.parseInt(hp.getText().toString()); } catch (NumberFormatException e) { hpCol.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); allValid = false; }
        try { atkVal = Integer.parseInt(attack.getText().toString()); } catch (NumberFormatException e) { attackCol.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); allValid = false; }
        try { defVal = Integer.parseInt(defense.getText().toString()); } catch (NumberFormatException e) { defenseCol.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); allValid = false; }

        levelVal = Integer.parseInt(levelSpinner.getSelectedItem().toString());
        if (levelVal < 1 || levelVal > 50) {
            errors.append("Level must be 1–50\n");
            allValid = false;
        }

        if (!allValid) {
            new AlertDialog.Builder(this)
                    .setTitle("Form Submission Errors")
                    .setMessage(errors.toString())
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(PokeDBHelper.COLUMN_NATIONAL_NUMBER, natNum);
        values.put(PokeDBHelper.COLUMN_NAME, nameVal);
        values.put(PokeDBHelper.COLUMN_SPECIES, speciesVal);
        values.put(PokeDBHelper.COLUMN_GENDER, genderVal);
        values.put(PokeDBHelper.COLUMN_HEIGHT, hVal);
        values.put(PokeDBHelper.COLUMN_WEIGHT, wVal);
        values.put(PokeDBHelper.COLUMN_LEVEL, levelVal);
        values.put(PokeDBHelper.COLUMN_HP, hpVal);
        values.put(PokeDBHelper.COLUMN_ATTACK, atkVal);
        values.put(PokeDBHelper.COLUMN_DEFENSE, defVal);

        Uri result = getContentResolver().insert(PokeContentProvider.CONTENT_URI, values);

        if (result != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Information stored in database!")
                    .setPositiveButton("OK", null)
                    .show();
            resetAllFields();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Duplicate entry! Not added.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}
