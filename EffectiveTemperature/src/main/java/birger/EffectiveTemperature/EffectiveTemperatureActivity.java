package birger.EffectiveTemperature;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import kankan.wheel.widget.OnWheelChangedListener;

public class EffectiveTemperatureActivity extends Activity implements OnWheelChangedListener {
	private WheelView temperature;
	private WheelView windspeed;
	private TextView result;

    final String deg_C  = "\u00b0C"; // °C
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        temperature = (WheelView) findViewById(R.id.temperature);
        temperature.setViewAdapter(new NumericWheelAdapter(this, -20, +5, "%+d"+deg_C, NumericWheelAdapter.SortOrder.SMALLEST_AT_BOTTOM));
        temperature.setCurrentItem(5); // start at 0°C
        temperature.setVisibleItems(3);
        temperature.addChangingListener(this);
        
        windspeed = (WheelView) findViewById(R.id.windspeed);
        windspeed.setViewAdapter(new NumericWheelAdapter(this, 0, 25, "%d m/s", NumericWheelAdapter.SortOrder.SMALLEST_AT_BOTTOM));
        windspeed.setCurrentItem(25); // start at 0 m/s
        windspeed.setVisibleItems(3);
        windspeed.addChangingListener(this);
        
        result = (TextView) findViewById(R.id.result);
        
        ((TextView) findViewById(R.id.yr_link)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.friluftsnett_link)).setMovementMethod(LinkMovementMethod.getInstance());
        
        recalculate();
    }
    
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
		recalculate();
	}
    
    public void recalculate() {
    	double T = 5 - temperature.getCurrentItem();
    	double v = 25 - windspeed.getCurrentItem(); // m/s
       	//result.setText(String.format("T=%.1f, v=%.1f", T, v));

    	if (v == 0){
    		result.setText(String.format("%.1f"+deg_C, T));
    	} else {
    		v *= 3.6; // m/s -> km/h
    		double T_eff = 13.12 + 0.6215*T - 11.37*Math.pow(v, 0.16) + 0.3965*T*Math.pow(v, 0.16);
    		result.setText(String.format("%.1f"+deg_C, T_eff));
    	}
    }
}