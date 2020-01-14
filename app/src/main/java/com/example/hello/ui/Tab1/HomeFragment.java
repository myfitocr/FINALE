package com.example.hello.ui.Tab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hello.FloatingWidgetService;
import com.example.hello.MainActivity;
import com.example.hello.PantsActivity;
import com.example.hello.R;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button createbtn;
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("PersonDB", Context.MODE_PRIVATE);


        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        createbtn = root.findViewById(R.id.create);

        final TextView totalLengthTextView = (TextView) root.findViewById(R.id.totalLegnth);
        final TextView waistTextView = (TextView) root.findViewById(R.id.waist);
        final TextView thighTextView = (TextView) root.findViewById(R.id.thigh);
        final TextView riseTextView = (TextView) root.findViewById(R.id.rise);
        final TextView hemcrossTextView = (TextView) root.findViewById(R.id.hemcross);

        if (!sharedPreferences.getString("totalLength", "not").equals("not")){
            totalLengthTextView.setText(sharedPreferences.getString("totalLength",""));
            waistTextView.setText(sharedPreferences.getString("waist",""));
            thighTextView.setText(sharedPreferences.getString("thigh",""));
            riseTextView.setText(sharedPreferences.getString("rise",""));
            hemcrossTextView.setText(sharedPreferences.getString("hemcross",""));
            System.out.println("tlqkf");
        }else{
//            System.out.println("tlqkf2");
//            totalLengthTextView.setText("96");
//            totalLengthTextView.setTextColor(Color.parseColor("#6d6d6d"));
//            waistTextView.setText("39");
//            waistTextView.setTextColor(Color.parseColor("#6d6d6d"));
//            thighTextView.setText("28.5");
//            thighTextView.setTextColor(Color.parseColor("#6d6d6d"));
//            riseTextView.setText("27");
//            riseTextView.setTextColor(Color.parseColor("#6d6d6d"));
//            hemcrossTextView.setText("16.5");
//            hemcrossTextView.setTextColor(Color.parseColor("#6d6d6d"));
        }
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalLengthTextView.getText().toString().equals("") || waistTextView.getText().toString().equals("") || thighTextView.getText().toString().equals("") ||
                        riseTextView.getText().toString().equals("") || hemcrossTextView.getText().toString().equals("")){
                    Toast.makeText(getContext(), "정보를 전부 기입해주세요~", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("totalLength", totalLengthTextView.getText().toString());
                    editor.putString("waist", waistTextView.getText().toString());
                    editor.putString("thigh", thighTextView.getText().toString());
                    editor.putString("rise", riseTextView.getText().toString());
                    editor.putString("hemcross", hemcrossTextView.getText().toString());
                    editor.apply();
                    editor.commit();
                    Log.v("test_1",(sharedPreferences.getString("totalLength","2")));
                    System.out.println("qudtls");
                    createFloatingWidget(v);
                }
            }
        });
        //ImageView imageView = getView().findViewById(R.id.imageView);
        /*Intent intent_1 = getIntent();
        Bitmap bitmap = intent_1.getParcelableExtra("image");
        imageView.setImageBitmap(bitmap);

         */
        return root;
    }





    /*  start floating widget service  */
    public void createFloatingWidget(View view) {
        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        //getcontext 부분 수정함.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission. 권한을 받기 위한 부분
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getActivity().getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        } else
            //If permission is granted start floating widget service
            startFloatingWidgetService();

    }

    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {
        getActivity().startService(new Intent(getActivity(), FloatingWidgetService.class));
        //이부분은 버튼 누르면 어플이 꺼지도록 함.
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK)
                //If permission granted start floating widget service
                startFloatingWidgetService();
            /**else
             //Permission is not available then display toast
             Toast.makeText(this,
             getResources().getString(R.string.draw_other_app_permission_denied),
             Toast.LENGTH_SHORT).show();*/

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}