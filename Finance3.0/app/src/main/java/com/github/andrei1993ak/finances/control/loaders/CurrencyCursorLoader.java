package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.util.ContextHolder;


public class CurrencyCursorLoader extends CursorLoader {

    private final DBHelperCurrency dbHelperCurrency;

    public CurrencyCursorLoader(final Context context) {
        super(context);
        dbHelperCurrency = ((DBHelperCurrency) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Currency.class));
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperCurrency.getAll();
    }
}
