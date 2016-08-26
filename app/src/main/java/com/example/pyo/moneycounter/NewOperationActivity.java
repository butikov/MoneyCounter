package com.example.pyo.moneycounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;

import java.util.ArrayList;

public class NewOperationActivity extends AppCompatActivity {

    GridLayout payersLayout;
    GridLayout debtorsLayout;
    private int debtsSum = 0, paysSum = 0, debtorsCount = 0;
    private int manNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CheckBox allDebt = (CheckBox) findViewById(R.id.allDebt);
        assert allDebt != null;
        allDebt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < manNumber; ++i) {
                        CheckBox debtorCheck = (CheckBox) debtorsLayout.findViewById(i * 2);
                        debtorCheck.setChecked(true);
                    }
                }
            }

        });

        CheckBox allEqual = (CheckBox) findViewById(R.id.allEqual);
        assert allEqual != null;
        allEqual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Integer debt = 0;
                    if (debtorsCount != 0) {
                        debt = paysSum / debtorsCount;
                        paysSum = debtsSum;
                    }
                    String str = debt.toString();
                    for (int i = 0; i < manNumber; ++i) {
                        CheckBox debtorCheck = (CheckBox) debtorsLayout.findViewById(i * 2);
                        if (debtorCheck.isChecked()) {
                            EditText debtorEdit = (EditText) debtorsLayout.findViewById(i * 2 + 1);
                            debtorEdit.setText(str);
                            debtorEdit.setEnabled(false);
                        }
                    }
                } else {
                    for (int i = 0; i < manNumber; ++i) {
                        CheckBox debtorCheck = (CheckBox) debtorsLayout.findViewById(i * 2);
                        if (debtorCheck.isChecked()) {
                            EditText debtorEdit = (EditText) debtorsLayout.findViewById(i * 2 + 1);
                            debtorEdit.setEnabled(true);
                        }
                    }
                }
            }
        });
        Intent intent = getIntent();
        ArrayList<String> names = intent.getStringArrayListExtra("names");
        manNumber = names.size();
        payersLayout = (GridLayout) findViewById(R.id.payersTable);
        debtorsLayout = (GridLayout) findViewById(R.id.debtorsTable);
        int nextId = 0;
        for (String name : names
                ) {
            CheckBox payerBox = new CheckBox(this);
            payerBox.setText(name);
            payerBox.setId(nextId);
            assert payersLayout != null;
            payersLayout.addView(payerBox);
            payerBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    assert payersLayout != null;
                    EditText edit = (EditText) payersLayout.findViewById(buttonView.getId() + 1);

                    String paidstr = edit.getText().toString();
                    if (!paidstr.isEmpty()) {
                        int paid = Integer.parseInt(edit.getText().toString());
                        if (edit.isEnabled())
                            paysSum -= paid;
                        else
                            paysSum += paid;
                        paidChanged();
                    }
                    edit.setEnabled(isChecked);
                }
            });
            EditText payerNumber = new EditText(this);
            payerNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
            payerNumber.setId(nextId + 1);
            payerNumber.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        paysSum = 0;
                        for (int i = 0; i < manNumber; ++i) {
                            CheckBox paysCheck = (CheckBox) payersLayout.findViewById(i * 2);
                            if (paysCheck.isChecked()) {
                                EditText paysEdit = (EditText) payersLayout.findViewById(i * 2 + 1);
                                debtsSum += Integer.parseInt(paysEdit.getText().toString());
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
            payerNumber.setEnabled(false);
            payersLayout.addView(payerNumber);

            CheckBox debtorsBox = new CheckBox(this);
            debtorsBox.setText(name);
            debtorsBox.setId(nextId);
            debtorsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                //TODO
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    assert debtorsLayout != null;
                    EditText edit = (EditText) debtorsLayout.findViewById(buttonView.getId() + 1);
                    String str = edit.getText().toString();
                    if (!str.isEmpty()) {
                        int debt = Integer.parseInt(str);
                        if (edit.isEnabled()) {
                            debtsSum -= debt;
                        } else {
                            debtsSum += debt;
                        }
                    }
                    edit.setEnabled(isChecked);
                }
            });
            assert debtorsLayout != null;
            debtorsLayout.addView(debtorsBox);
            EditText debtorNumber = new EditText(this);
            debtorNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
            debtorNumber.setId(nextId + 1);
            debtorNumber.setEnabled(false);
            debtorNumber.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        debtsSum = 0;
                        for (int i = 0; i < manNumber; ++i) {
                            CheckBox debtCheck = (CheckBox) debtorsLayout.findViewById(i * 2);
                            if (debtCheck.isChecked()) {
                                EditText debtEdit = (EditText) debtorsLayout.findViewById(i * 2 + 1);
                                debtsSum += Integer.parseInt(debtEdit.getText().toString());
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
            debtorsLayout.addView(debtorNumber);
            nextId += 2;
        }
    }

    public void operationDone(View view) {
        if (paysSum != debtsSum)
            return;
        int[] pays = new int[manNumber * 2];
        CheckBox debtorBox;
        EditText debtorView;
        CheckBox payerBox;
        EditText payerEdit;
        for (int i = 0; i < manNumber * 2; i += 2) {
            assert payersLayout != null;
            payerBox = (CheckBox) payersLayout.findViewById(i);
            payerEdit = (EditText) payersLayout.findViewById(i + 1);
            if (payerBox.isChecked()) {
                int number = Integer.parseInt(payerEdit.getText().toString());
                if (number <= 0)
                    return;
                pays[i] = number;
            } else
                pays[i] = 0;

            assert debtorsLayout != null;
            debtorBox = (CheckBox) debtorsLayout.findViewById(i);
            debtorView = (EditText) debtorsLayout.findViewById(i + 1);
            if (debtorBox.isChecked()) {
                int number = Integer.parseInt(debtorView.getText().toString());
                if (number <= 0)
                    return;
                pays[manNumber + i] = number;
            } else
                pays[manNumber + i] = 0;
        }


        Intent intent = new Intent();
        intent.putExtra("pays", pays);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void paidChanged() {
        CheckBox allEqual = (CheckBox) findViewById(R.id.allEqual);
        assert allEqual != null;
        if (!allEqual.isChecked() || debtorsCount == 0)
            return;
        Integer debt = paysSum / debtorsCount;
        String str = debt.toString();
        for (int i = 0; i < manNumber; ++i) {
            EditText debtEdit = (EditText) debtorsLayout.findViewById(i * 2 + 1);
            debtEdit.setEnabled(false);
            debtEdit.setText(str);
        }
    }

}
