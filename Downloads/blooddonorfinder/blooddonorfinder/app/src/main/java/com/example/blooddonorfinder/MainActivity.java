
package com.example.blooddonorfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText etName, etAddress, etDistrict, etSearch;
    private Spinner spBloodGroup;
    private LinearLayout layoutDonors;
    private DatabaseHelper databaseHelper;
    private int selectedId = -1;

    private final String[] bloodGroups = {
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etDistrict = findViewById(R.id.etDistrict);
        etSearch = findViewById(R.id.etSearch);
        spBloodGroup = findViewById(R.id.spBloodGroup);
        layoutDonors = findViewById(R.id.layoutDonors);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnClear = findViewById(R.id.btnClear);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnViewAll = findViewById(R.id.btnViewAll);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                bloodGroups
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spBloodGroup.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> addDonor());
        btnUpdate.setOnClickListener(v -> updateDonor());
        btnClear.setOnClickListener(v -> clearForm());
        btnSearch.setOnClickListener(v ->
                displayDonors(etSearch.getText().toString()));

        btnViewAll.setOnClickListener(v -> {
            etSearch.setText("");
            displayDonors("");
        });

        displayDonors("");
    }

    private boolean validateForm() {
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Enter name");
            etName.requestFocus();
            return false;
        }

        if (etAddress.getText().toString().trim().isEmpty()) {
            etAddress.setError("Enter address");
            etAddress.requestFocus();
            return false;
        }

        if (etDistrict.getText().toString().trim().isEmpty()) {
            etDistrict.setError("Enter district");
            etDistrict.requestFocus();
            return false;
        }

        return true;
    }

    private void addDonor() {
        if (!validateForm()) return;

        boolean success = databaseHelper.addDonor(
                etName.getText().toString().trim(),
                etAddress.getText().toString().trim(),
                spBloodGroup.getSelectedItem().toString(),
                etDistrict.getText().toString().trim()
        );

        if (success) {
            Toast.makeText(this, "Donor added successfully",
                    Toast.LENGTH_SHORT).show();
            clearForm();
            displayDonors("");
        } else {
            Toast.makeText(this, "Unable to add donor",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDonor() {
        if (selectedId == -1) {
            Toast.makeText(this, "Select a donor using Edit first",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateForm()) return;

        boolean success = databaseHelper.updateDonor(
                selectedId,
                etName.getText().toString().trim(),
                etAddress.getText().toString().trim(),
                spBloodGroup.getSelectedItem().toString(),
                etDistrict.getText().toString().trim()
        );

        if (success) {
            Toast.makeText(this, "Donor updated successfully",
                    Toast.LENGTH_SHORT).show();
            clearForm();
            displayDonors(etSearch.getText().toString());
        } else {
            Toast.makeText(this, "Unable to update donor",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void displayDonors(String keyword) {
        layoutDonors.removeAllViews();

        Cursor cursor = databaseHelper.getDonors(keyword);
        int count = 0;

        try {
            while (cursor.moveToNext()) {
                count++;

                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(
                        cursor.getColumnIndexOrThrow("address"));
                String bloodGroup = cursor.getString(
                        cursor.getColumnIndexOrThrow("blood_group"));
                String district = cursor.getString(
                        cursor.getColumnIndexOrThrow("district"));

                addDonorCard(id, name, address, bloodGroup, district);
            }
        } finally {
            cursor.close();
        }

        if (count == 0) {
            TextView empty = new TextView(this);
            empty.setText("No donors found.");
            empty.setTextSize(16);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(8, 20, 8, 20);
            layoutDonors.addView(empty);
        }
    }

    private void addDonorCard(int id, String name, String address,
                              String bloodGroup, String district) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(16, 12, 16, 12);
        card.setBackgroundColor(Color.rgb(235, 248, 238));

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 12);
        card.setLayoutParams(cardParams);

        TextView details = new TextView(this);
        details.setText("Name: " + name +
                "\nAddress: " + address +
                "\nBlood Group: " + bloodGroup +
                "\nDistrict: " + district);
        details.setTextSize(16);
        details.setTextColor(Color.BLACK);
        details.setPadding(0, 0, 0, 8);
        card.addView(details);

        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);

        Button edit = new Button(this);
        edit.setText("Edit");

        Button delete = new Button(this);
        delete.setText("Delete");

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        buttons.addView(edit, buttonParams);
        buttons.addView(delete, buttonParams);
        card.addView(buttons);
        layoutDonors.addView(card);

        edit.setOnClickListener(v -> {
            selectedId = id;
            etName.setText(name);
            etAddress.setText(address);
            etDistrict.setText(district);

            for (int i = 0; i < bloodGroups.length; i++) {
                if (bloodGroups[i].equals(bloodGroup)) {
                    spBloodGroup.setSelection(i);
                    break;
                }
            }

            Toast.makeText(this, "Edit details and press Update",
                    Toast.LENGTH_SHORT).show();
        });

        delete.setOnClickListener(v ->
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Donor")
                        .setMessage("Delete donor " + name + "?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            if (databaseHelper.deleteDonor(id)) {
                                if (selectedId == id) clearForm();

                                Toast.makeText(MainActivity.this,
                                        "Donor deleted",
                                        Toast.LENGTH_SHORT).show();

                                displayDonors(
                                        etSearch.getText().toString());
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Unable to delete donor",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show());
    }

    private void clearForm() {
        etName.setText("");
        etAddress.setText("");
        etDistrict.setText("");
        spBloodGroup.setSelection(0);
        selectedId = -1;
        etName.requestFocus();
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}
