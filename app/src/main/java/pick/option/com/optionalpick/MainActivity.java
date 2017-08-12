package pick.option.com.optionalpick;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import cn.qqtheme.framework.picker.MultiPicker;

public class MainActivity extends AppCompatActivity implements MultiPicker.OnMultiPickListener,View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViewById(R.id.init).setOnClickListener(this);
        this.findViewById(R.id.show).setOnClickListener(this);
    }

    @Override
    public void onMultiPick(String item1, String item2) {
        Toast.makeText(this,item1+","+item2,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.init){

            MultiPickerManager.init(this,"division.json" ); // application起动时初始化

        }else if(v.getId() == R.id.show){

            MultiPickerManager.build(this)
                    .setTitleBackgroundColor(Color.parseColor("#CD9C34"))
                    .setItems(MultiPickerManager.getOptionalData())
                    .setOnMultiPickListener(this)
                    .show();

        }
    }
}
