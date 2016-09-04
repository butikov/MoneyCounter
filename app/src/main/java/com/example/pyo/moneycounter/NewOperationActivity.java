package com.example.pyo.moneycounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;

import java.util.ArrayList;

public class NewOperationActivity extends AppCompatActivity {

    GridLayout payersLayout;
    GridLayout debtorsLayout;
    boolean allEquals = false;
    private int debtsSum = 0, paysSum = 0, debtorsCount = 0;
    private int manNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("Добавить операцию");
        setSupportActionBar(toolbar);
        Button allDebt = (Button) findViewById(R.id.allCheck);
        assert allDebt != null;
        Intent intent = getIntent();
        ArrayList<String> names = intent.getStringArrayListExtra("names");
        manNumber = names.size();
        payersLayout = (GridLayout) findViewById(R.id.payersTable);
        debtorsLayout = (GridLayout) findViewById(R.id.debtorsTable);
        allDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < manNumber; ++i) {
                    CheckBox debtorCheck = (CheckBox) debtorsLayout.findViewById(i * 2);
                    debtorCheck.setChecked(true);
                    paidChanged();
                }
            }
        });
        CheckBox allEqual = (CheckBox) findViewById(R.id.allEqual);
        assert allEqual != null;
        allEqual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                allEquals = isChecked;
                for (int i = 0; i < manNumber; ++i) {
                    CheckBox debtorCheck = (CheckBox) debtorsLayout.findViewById(i * 2);
                    if (debtorCheck.isChecked()) {
                        EditText debtorEdit = (EditText) debtorsLayout.findViewById(i * 2 + 1);
                        debtorEdit.setEnabled(!isChecked);
                    }
                }
                paidChanged();
            }
        });
        Button allUncheck = (Button) findViewById(R.id.allUncheck);
        assert allUncheck != null;
        allUncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < manNumber; ++i) {
                    CheckBox debtorCheck = (CheckBox) debtorsLayout.findViewById(i * 2);
                    debtorCheck.setChecked(false);
                    paidChanged();
                }
            }
        });
        int nextId = 0;
        for (String name : names) {
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
            payerNumber.addTextChangedListener(new TextWatcher() {
                int previousValue;
                int currentValue;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (s.toString().length() > 0) {
                        try {
                            previousValue = Integer.parseInt(s.toString());
                        } catch (Exception e) {
                            previousValue = 0;
                        }
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() > 0) {
                        try {
                            currentValue = Integer.parseInt(s.toString());
                        } catch (Exception e) {
                            currentValue = 0;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    paysSum -= previousValue;
                    paysSum += currentValue;
                    paidChanged();
                }
            });
            payerNumber.setEnabled(false);
            payerNumber.setText("0");
            payersLayout.addView(payerNumber);

            CheckBox debtorsBox = new CheckBox(this);
            debtorsBox.setText(name);
            debtorsBox.setId(nextId);
            debtorsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    assert debtorsLayout != null;
                    EditText edit = (EditText) debtorsLayout.findViewById(buttonView.getId() + 1);
                    String str = edit.getText().toString();
                    if (!str.isEmpty()) {
                        int debt = Integer.parseInt(str);
                        if (isChecked) {
                            debtsSum += debt;
                            ++debtorsCount;
                        } else {
                            debtsSum -= debt;
                            --debtorsCount;
                        }
                    }
                    if (!allEquals) {
                        edit.setEnabled(isChecked);
                    }
                }
            });
            assert debtorsLayout != null;
            debtorsLayout.addView(debtorsBox);
            EditText debtorNumber = new EditText(this);
            debtorNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
            debtorNumber.setId(nextId + 1);
            debtorNumber.setEnabled(false);
            debtorNumber.addTextChangedListener(new TextWatcher() {
                int previousValue;
                int currentValue;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (s.toString().length() > 0) {
                        try {
                            previousValue = Integer.parseInt(s.toString());
                        } catch (Exception e) {
                            previousValue = 0;
                        }
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() > 0) {
                        try {
                            currentValue = Integer.parseInt(s.toString());
                        } catch (Exception e) {
                            currentValue = 0;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    debtsSum -= previousValue;
                    debtsSum += currentValue;
                }
            });
            debtorNumber.setText("0");
            debtorsLayout.addView(debtorNumber);
            nextId += 2;
        }
    }

    public void operationDone(View view) {
        if (paysSum != debtsSum) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Сумма потраченных денег не равна сумме долгов!").setPositiveButton("Понятно",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
            return;
        }
        if (paysSum == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Пустая операция!").setPositiveButton("Понятно",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
            return;
        }
        int[] pays = new int[manNumber * 2];
        CheckBox debtorBox;
        EditText debtorView;
        CheckBox payerBox;
        EditText payerEdit;
        for (Integer i = 0; i < manNumber * 2; i += 2) {
            assert payersLayout != null;
            payerBox = (CheckBox) payersLayout.findViewById(i);
            payerEdit = (EditText) payersLayout.findViewById(i + 1);
            if (payerBox.isChecked()) {
                int number = Integer.parseInt(payerEdit.getText().toString());
                if (number <= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Участник " + i.toString() + " выбран, но не платил!").setPositiveButton("Понятно",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                    return;
                }
                pays[manNumber + i / 2] = number;
            } else
                pays[manNumber + i / 2] = 0;
            assert debtorsLayout != null;
            debtorBox = (CheckBox) debtorsLayout.findViewById(i);
            debtorView = (EditText) debtorsLayout.findViewById(i + 1);
            if (debtorBox.isChecked()) {
                int number = Integer.parseInt(debtorView.getText().toString());
                if (number <= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Участник " + i.toString() + " выбран, но не задолжал!").setPositiveButton("Понятно",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                    return;
                }
                pays[i / 2] = number;
            } else
                pays[i / 2] = 0;
        }
        Intent intent = new Intent();
        intent.putExtra("pays", pays);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void paidChanged() {
        if (!allEquals || debtorsCount == 0)
            return;
        Integer debt = paysSum / debtorsCount;
        String str = debt.toString();
        for (int i = 0; i < manNumber; ++i) {
            CheckBox checked = (CheckBox) debtorsLayout.findViewById(i * 2);
            if (checked.isChecked()) {
                EditText debtEdit = (EditText) debtorsLayout.findViewById(i * 2 + 1);
                debtEdit.setEnabled(false);
                debtEdit.setText(str);
            }
        }
        debtsSum = paysSum;
    }

}
