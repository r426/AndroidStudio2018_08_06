package lt.kaunascoding.myapplication.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;


import java.util.ArrayList;

import lt.kaunascoding.myapplication.Controller.MainActivity;
import lt.kaunascoding.myapplication.Model.DBActions;
import lt.kaunascoding.myapplication.Model.ItemVO;
import lt.kaunascoding.myapplication.R;


public class ItemVOAdapter extends ArrayAdapter<ItemVO> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    // Model
    private DBActions dbActions;

    public ItemVOAdapter(Context context, ArrayList<ItemVO> items, DBActions dbActions) {
        super(context, 0, items);
        this.dbActions = dbActions;
    }

    @Override
    public void onClick(View view) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        int position = listView.getPositionForView(parentRow);

        ItemVO itemVO = getItem(position);
        dbActions.deleteItem(itemVO);
        ((MainActivity) getContext()).updateView();
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();

        if (listView == null) {
            return;
        }

        int position = listView.getPositionForView(parentRow);

        ItemVO itemVO = getItem(position);

        if(isChecked == true){
            itemVO.done=1;
        }else{
            itemVO.done=0;
        }
        dbActions.updateItem(itemVO);

        ((MainActivity) getContext()).updateView();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemVO item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_vo, parent, false);
        }

        CheckBox itemCheckbox = (CheckBox) convertView.findViewById(R.id.item_checkbox);

        itemCheckbox.setText(item.title);
        itemCheckbox.setChecked(item.done == 1);

        itemCheckbox.setOnCheckedChangeListener(this);

        Button deleteButton = (Button) convertView.findViewById(R.id.item_delete);
        deleteButton.setOnClickListener(this);

        return convertView;
    }


}