package com.github.andrei1993ak.finances.app.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.control.base.IOnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CostCategoryExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.CostCategory;

import java.util.List;

public class CostCategoryAddActivity extends BaseActivity implements IOnTaskCompleted {

    private EditText newCategoryName;
    private AppCompatSpinner parentCategories;
    private List<CostCategory> parentsList;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add_edit_activity);
        setTitle(R.string.newCategory);

        initFields();

        new CostCategoryExecutor(this).execute(new RequestAdapter<CostCategory>().getAllToList(RequestAdapter.SELECTION_PARENT_CATEGORIES));
    }

    private void initFields() {
        this.newCategoryName = (EditText) findViewById(R.id.add_edit_category_name);
        this.parentCategories = (AppCompatSpinner) findViewById(R.id.spinnerParentCategories);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cat_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addCategory();
            }
        });
    }

    public void addCategory() {
        final CostCategory costCategory = checkFields();
        if (costCategory != null) {
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(CostCategory.class), costCategory);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Nullable
    private CostCategory checkFields() {
        final CostCategory costCategory = new CostCategory();
        boolean flag = true;
        if (newCategoryName.getText().toString().length() == 0) {
            newCategoryName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            newCategoryName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            costCategory.setName(newCategoryName.getText().toString());
        }
        if (spinnerAdapter == null) {
            parentCategories.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            parentCategories.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            if (parentCategories.getSelectedItemPosition() == spinnerAdapter.getCount() - 1) {
                costCategory.setParentId(-1L);
            } else {
                costCategory.setParentId(parentsList.get(parentCategories.getSelectedItemPosition()).getId());
            }
        }
        if (!flag) {
            return null;
        } else {
            return costCategory;
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case CostCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                parentsList = (List<CostCategory>) result.getObject();
                final String[] names = new String[parentsList.size() + 1];
                int i = 0;
                for (final CostCategory costCategory : parentsList) {
                    names[i++] = costCategory.getName();
                }
                names[i] = "-";
                spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                parentCategories.setAdapter(spinnerAdapter);
                parentCategories.setSelection(spinnerAdapter.getCount() - 1);
                break;
        }
    }
}
