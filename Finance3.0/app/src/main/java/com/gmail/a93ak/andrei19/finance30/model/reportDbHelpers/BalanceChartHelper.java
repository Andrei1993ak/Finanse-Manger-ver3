package com.gmail.a93ak.andrei19.finance30.model.reportDbHelpers;

import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCost;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperIncome;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperPurse;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperTransfer;
import com.gmail.a93ak.andrei19.finance30.model.models.Cost;
import com.gmail.a93ak.andrei19.finance30.model.models.Income;
import com.gmail.a93ak.andrei19.finance30.model.models.Transfer;

import org.achartengine.model.TimeSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BalanceChartHelper {

    private static BalanceChartHelper instance;

    public static BalanceChartHelper getInstance() {
        if (instance == null)
            instance = new BalanceChartHelper();
        return instance;
    }

    public TimeSeries getSeries(final long purseId) {
        final List<Income> incomesList = DBHelperIncome.getInstance().getAllToListByPurseId(purseId);
        final List<Cost> costsList = DBHelperCost.getInstance().getAllToListByPurseId(purseId);
        final List<Transfer> transferList = DBHelperTransfer.getInstance().getAllToListByPurseId(purseId);

        final HashMap<Date, Double> operations = new HashMap<>();

        for (final Income income : incomesList) {
            final Date date = new Date(income.getDate());
            if (operations.containsKey(date)) {
                operations.put(date, operations.get(date) + income.getAmount());
            } else {
                operations.put(date, income.getAmount());
            }
        }
        for (final Cost cost : costsList) {
            final Date date = new Date(cost.getDate());
            if (operations.containsKey(date)) {
                operations.put(date, operations.get(date) - cost.getAmount());
            } else {
                operations.put(date, -cost.getAmount());
            }
        }
        for (final Transfer transfer : transferList) {
            final Date date = new Date(transfer.getDate());
            if (operations.containsKey(date)) {
                if (transfer.getFromPurseId() == purseId) {
                    operations.put(date, operations.get(date) - transfer.getFromAmount());
                } else {
                    operations.put(date, operations.get(date) + transfer.getToAmount());
                }
            } else {
                if (transfer.getFromPurseId() == purseId) {
                    operations.put(date, -transfer.getFromAmount());
                } else {
                    operations.put(date, transfer.getToAmount());
                }
            }
        }

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final TimeSeries series = new TimeSeries("Balance");
        Double amount = DBHelperPurse.getInstance().get(purseId).getAmount();
        int count = 0;
        for (final Date date = calendar.getTime(); count < operations.size(); date.setTime(date.getTime() - 86400000)) {
            if (operations.containsKey(date)) {
                amount -= operations.get(date);
                count++;
            }
            series.add(date, amount);
        }
        return series;
    }
}
