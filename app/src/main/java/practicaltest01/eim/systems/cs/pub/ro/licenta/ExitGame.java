package practicaltest01.eim.systems.cs.pub.ro.licenta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExitGame extends AppCompatActivity {
    Button yes, no;

    private OnClickButtonListener onClickButtonListener =  new OnClickButtonListener();

    private class OnClickButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.yes:
                    setResult(RESULT_OK, null);
                    break;
                case R.id.no:
                    setResult(RESULT_CANCELED, null);
                    break;
            }
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_game);

        yes = (Button)findViewById(R.id.yes);
        no = (Button)findViewById(R.id.no);

        yes.setOnClickListener(onClickButtonListener);
        no.setOnClickListener(onClickButtonListener);
    }
}
