package view_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ysk.android_project.R;

import java.util.ArrayList;

import vo.TimerAddVO;

public class ViewModelAdapter extends ArrayAdapter<TimerAddVO> {

    Context context;
    ArrayList<TimerAddVO> list;
    TimerAddVO vo;
    int resource;

    public ViewModelAdapter(Context context, int resource,ArrayList<TimerAddVO> list, ListView newTimer) {
        super(context, resource, list);

        this.context = context;
        this.list = list;
        this.resource = resource;

        newTimer.setOnItemClickListener(list_click);

    }

    AdapterView.OnItemClickListener list_click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent;
            //추가할 해당 타이머 페이지 이동
            switch (list.get(i).getTitle()){

                case "준비":
                case "운동":
                case "휴식":
                    intent = new Intent();
                    break;

                case "라운드":
                    intent = new Intent();
                    break;

            }



        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        convertView = linf.inflate(resource,null);

        vo = list.get(position);

        TextView title = convertView.findViewById(R.id.title);
        TextView sub_title = convertView.findViewById(R.id.sub_title);
        TextView timer = convertView.findViewById(R.id.timer);

        title.setText(vo.getTitle());
        sub_title.setText(vo.getSub_title());
        timer.setText(vo.getTimer());

        return convertView;

    }

}
