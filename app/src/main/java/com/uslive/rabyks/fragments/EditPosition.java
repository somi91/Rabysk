package com.uslive.rabyks.fragments;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.uslive.rabyks.R;
import com.uslive.rabyks.dialogs.EmployeeReservationDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by milos on 6/5/2015.
 */
public class EditPosition extends Fragment {
    int sdk = android.os.Build.VERSION.SDK_INT;
    RelativeLayout edit_content;
    float scale;

    private JSONArray partnerSetup;

    private boolean free;
    private boolean delete;

    private int partnerId;
    private int objectId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.edit_position_layout, container, false);
        edit_content = (RelativeLayout) view.findViewById(R.id.placeHolder);
        setBackGroundImage();
        scale = getActivity().getApplicationContext().getResources().getDisplayMetrics().density;

        int width;
        int height;

        Bundle mArgs = getArguments();
        if(mArgs != null){
            String partnerSetupString = mArgs.getString("partnerSetup");
            partnerId = mArgs.getInt("partnerId");

            width = mArgs.getInt("layoutWidth");
            height = mArgs.getInt("layoutHeight");
            Log.d("width and height ", " "+width+"dp  "+height+"dp " );
            edit_content.setMinimumWidth(width);
            edit_content.setMinimumHeight(height);
            Log.i("EDIT POSITION FRAGMENT ", partnerSetupString);
            try {
                JSONArray jsonArray = new JSONArray(partnerSetupString);
                initialPartnerSetup(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        Button btn1 = new Button(getActivity().getApplicationContext());
//        setButton(btn1, 50, 100, Color.GREEN);
//        Button btn2 = new Button(getActivity().getApplicationContext());
//        setButton(btn2, 100, 200, Color.RED);
//        Button btn3 = new Button(getActivity().getApplicationContext());
//        setButton(btn3, 200, 120, Color.GREEN);
//        Button btn4 = new Button(getActivity().getApplicationContext());
//        setButton(btn4, 200, 220, Color.RED);

        view.findViewById(R.id.sto).setOnTouchListener(new MyTouchListener());
        view.findViewById(R.id.separe).setOnTouchListener(new MyTouchListener());
        view.findViewById(R.id.stajanje).setOnTouchListener(new MyTouchListener());

        view.findViewById(R.id.placeHolder).setOnDragListener(new MyDragListener());

        // Setup handles to view objects here
        // etFoo = (EditText) view.findViewById(R.id.etFoo);
        return view;
    }

    public void setBackGroundImage(){

        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            edit_content.setBackgroundDrawable(getResources().getDrawable(R.drawable.sample_0, null) );
        } else {
            edit_content.setBackground(getResources().getDrawable(R.drawable.sample_0, null));
        }
    }

    public void setButton(Button btn, final int x, final int y, int color, final JSONObject obj){
        final int pixelsX = (int) (x * scale + 0.5f);
        final int pixelY = (int) (y * scale + 0.5f);
        ShapeDrawable biggerCircle= new ShapeDrawable( new OvalShape());
        biggerCircle.setIntrinsicHeight( 50 );
        biggerCircle.setIntrinsicWidth( 50);
        biggerCircle.setBounds(new Rect(0, 0, 50, 50));
        biggerCircle.getPaint().setColor(Color.WHITE);

        ShapeDrawable smallerCircle= new ShapeDrawable( new OvalShape());
        smallerCircle.setIntrinsicHeight( 10 );
        smallerCircle.setIntrinsicWidth( 10);
        smallerCircle.setBounds(new Rect(0, 0, 10, 10));
        smallerCircle.getPaint().setColor(color);
        smallerCircle.setPadding(40,40,40,40);
        Drawable[] d = {smallerCircle,biggerCircle};

        LayerDrawable composite1 = new LayerDrawable(d);

        btn = new Button(getActivity().getApplicationContext());
        btn.setX(pixelsX);
        btn.setY(pixelY);
        btn.setLayoutParams(new LinearLayout.LayoutParams(140, 140));
        btn.setBackgroundDrawable(composite1);
        btn.setBackground(composite1);
        btn.setText("4");
        btn.setTextColor(Color.BLACK);
        edit_content.addView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "x: " + pixelsX + " y: " + pixelY, Toast.LENGTH_LONG).show();
                showDialog(obj);
            }
        });

    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                if(view.getId() == R.id.sto){
                    ClipData data = ClipData.newPlainText("", "");
                    Button btn = setButton(Color.GREEN);
                    view.startDrag(data, shadowBuilder, btn, 0);
                }else if(view.getId() == R.id.separe){
                    ImageButton btn = new ImageButton(getActivity().getApplicationContext());
                    btn.setBackgroundResource(R.drawable.star);
                    btn.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
                    ClipData dataSepare = ClipData.newPlainText("dataSepare", "SEPARE");
                    view.startDrag(dataSepare, shadowBuilder, btn, 0);
                }else {
                    ImageButton btn = new ImageButton(getActivity().getApplicationContext());
                    btn.setBackgroundResource(R.drawable.man);
                    btn.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
                    ClipData dataStajanje = ClipData.newPlainText("dataSepare", "SEPARE");
                    view.startDrag(dataStajanje, shadowBuilder, btn, 0);
                }
//                btn.startDrag(data, shadowBuilder, view, 0);
//                view.startDrag(data, shadowBuilder, btn, 0);
//                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
//                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
//                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();

                    float x = event.getX();
                    float y = event.getY();
                    RelativeLayout container = (RelativeLayout) v;
//                    container.addView(view);
                    ViewGroup owner = (ViewGroup) view.getParent();
                    float width = 0;
                    float height = 0;
                    Log.d("My message: ", event.getClipData().getItemAt(0).getText().toString());
//                        owner.removeView(view);
                    width = view.getLayoutParams().width / 2;
                    height = view.getLayoutParams().height / 2;

                    final float real_coordinate_x = x - width;
                    final float real_coordinate_y = y - height;

//                    final int pixelsX = (int) (real_coordinate_x * scale + 0.5f);
//                    final int pixelY = (int) (real_coordinate_y * scale + 0.5f);

                    view.setX(real_coordinate_x);
                    view.setY(real_coordinate_y);
                    final JSONObject jsonObject = new JSONObject();
                    Random r = new Random();
                    try {
                        jsonObject.put("objectId", r);
                        jsonObject.put("numberOfSeats", r);
                        jsonObject.put("timeOut", r);
                        jsonObject.put("availability", true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity().getApplicationContext(), "x: " + real_coordinate_x + " y: " + real_coordinate_y, Toast.LENGTH_LONG).show();
                            showDialog(jsonObject);
                        }
                    });
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
//                    v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    public Button setButton(int color){
        ShapeDrawable biggerCircle= new ShapeDrawable( new OvalShape());
        biggerCircle.setIntrinsicHeight( 50 );
        biggerCircle.setIntrinsicWidth( 50);
        biggerCircle.setBounds(new Rect(0, 0, 50, 50));
        biggerCircle.getPaint().setColor(Color.WHITE);

        ShapeDrawable smallerCircle= new ShapeDrawable( new OvalShape());
        smallerCircle.setIntrinsicHeight( 10 );
        smallerCircle.setIntrinsicWidth( 10);
        smallerCircle.setBounds(new Rect(0, 0, 10, 10));
        smallerCircle.getPaint().setColor(color);
        smallerCircle.setPadding(40,40,40,40);
        Drawable[] d = {smallerCircle,biggerCircle};

        LayerDrawable composite1 = new LayerDrawable(d);
        Button btn = new Button(getActivity().getApplicationContext());
        btn.setLayoutParams(new LinearLayout.LayoutParams(140, 140));
        btn.setBackgroundDrawable(composite1);
        btn.setBackground(composite1);
        btn.setText("4");
        btn.setTextColor(Color.BLACK);

        return btn;

    }

    void showDialog(JSONObject obj) {
        Bundle args = new Bundle();
        boolean free = true;
        try {
            free = obj.getBoolean("availability");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            args.putInt("partnerId", partnerId);
            args.putInt("objectId", obj.getInt("objectId"));
            args.putInt("numberOfSeats", obj.getInt("numberOfSeats"));
            args.putInt("timeOut", obj.getInt("timeOut"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        FragmentManager fm = getActivity().getSupportFragmentManager();
        args.putBoolean("free", free);
        EmployeeReservationDialog employeeReservationDialog = new EmployeeReservationDialog();
        employeeReservationDialog.setArguments(args);
        employeeReservationDialog.show(fm, "employeeReservationDialog");
    }

    private void initialPartnerSetup(JSONArray partnerSetup) {
        Log.i("initial Partner Setup", partnerSetup.toString());
        for (int i = 0; i < partnerSetup.length(); i++) {
            Button btn = new Button(getActivity().getApplicationContext());
            try {
                JSONObject obj = partnerSetup.getJSONObject(i);
                if(obj.getString("type").equals("sto")){
                    Log.i("Type sto", obj.getString("type"));
                    if (obj.getBoolean("availability")) {
                        Log.i("availability", obj.getBoolean("availability")+"");
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.GREEN, obj);
                    } else {
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.RED, obj);
                    }
                } else if (obj.getString("type").equals("separe")) {
                    Log.i("Type separe", obj.getString("type"));
                    if (obj.getBoolean("availability")) {
                        Log.i("availability", obj.getBoolean("availability")+"");
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.GREEN, obj);
                    } else {
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.RED, obj);
                    }
                } else {
                    Log.i("Type stajanje", obj.getString("type"));
                    if (obj.getBoolean("availability")) {
                        Log.i("availability", obj.getBoolean("availability")+"");
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.GREEN, obj);
                    } else {
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.RED, obj);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void Save(){
        /*
        primer kako treba da izgleda objekat koji saljem na server
        <string name="savePositionURL">http://ec2-52-25-43-102.us-west-2.compute.amazonaws.com:80/postPartnerObjectSetup</string>
        {
            "_id" : ObjectId("55870b6b8364823670823923"),
            "partnerId" : 4,
            "defaultTableSeatCount" : 1,
            "defaultSepareSeatCount" : 1,
            "defaultBarseatSeatCount" : 1,
            "defaultStandSeatCount" : 1,
            "objects" : [
                { "objectId" : 1, "type" : "separe", "timeOut" : 1, "price" : 1, "availability" : true, "numberOfSeats" : 5, "coordinateX" : 50, "coordinateY" : 150 },
                { "objectId" : 2, "type" : "sto", "timeOut" : 1, "price" : 1, "availability" : true, "numberOfSeats" : 4, "coordinateX" : 220, "coordinateY" : 40 },
                { "objectId" : 3, "type" : "bar", "timeOut" : 1, "price" : 1, "availability" : true, "numberOfSeats" : 1, "coordinateX" : 80, "coordinateY" : 100 }
            ]
        }
         */
    }
}
