package net.Reifman.midterm;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;


//TipIt!
public class MainActivity extends Activity implements OnItemSelectedListener, OnClickListener, OnEditorActionListener {
	//Declare all variables for wiring
	private Spinner countrySpin;
	private TextView tipThisMuch;
	private EditText billAmountEdit;
	private TextView tipPercentEdit;
	private TextView totalAmountEdit;
	private Button btnDecrement;
	private Button btnIncrement;
	private int tipPercent;
	private TextView tipAmount;
	
	
	DecimalFormat money = new DecimalFormat("#,##0.00"); //Super scary global variable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Wiring starts
        billAmountEdit = (EditText)findViewById(R.id.billAmount);
        tipPercentEdit = (TextView)findViewById(R.id.tipPercent);
        tipThisMuch = (TextView)findViewById(R.id.tipThisMuch);
        totalAmountEdit = (TextView)findViewById(R.id.totalAmount);
        btnDecrement = (Button)findViewById(R.id.decrement);
        btnIncrement = (Button)findViewById(R.id.increment);
        tipAmount = (TextView)findViewById(R.id.tipAmount);
        
        countrySpin = (Spinner)findViewById(R.id.countryChoice);
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.country_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        countrySpin.setAdapter(adapter);
        countrySpin.setSelection(0); //sets to first item
        countrySpin.setOnItemSelectedListener(this);
        
        billAmountEdit.setOnEditorActionListener(this);
        countrySpin.setSelection(0); //sets to first item
        countrySpin.setOnItemSelectedListener(this);
        btnDecrement.setOnClickListener(this);
        btnIncrement.setOnClickListener(this);
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	String reset = getString(R.string.reset);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
       /** case R.id.action_settings:
        	Toast.makeText(this, "Place Holder", Toast.LENGTH_SHORT).show();
        	return true; **/
        case R.id.action_reset: //Reset everything to 0
        	onCreate(null);
        case R.id.action_about: //Who is this about
        	Toast.makeText(this,  "Created by Andrew Reifman-Packett", Toast.LENGTH_LONG).show();
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }



	
	public void onNothingSelected(AdapterView<?> parent){
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//Australia, France, Germany, Italy, Mexico, New Zealand, Portugal, Sweden, Switzerland UK, USA
		 Resources res = getResources();
         String[] howMuchTip = res.getStringArray(R.array.thisCountry);
    	 //int pos = countrySpin.getSelectedItemPosition();
         String answer = "";
         String builder = "When in "+ (String)countrySpin.getSelectedItem() + " it's customary to tip: ";
         String noTip = "When in "+ (String)countrySpin.getSelectedItem() + " it's customary not to tip unless you feel you received exceptional service. ";
         if(howMuchTip[position].equals(""))
        	 answer = noTip;
         else
        	 answer = builder + howMuchTip[position];
         calculate();
         String[] parts = howMuchTip[position].split("\\.");
         String temp; 
         if(parts[0].equals("")){
        	 tipPercent = 0;
        	 temp = tipPercent +"%";
         }
         else{
        	 tipPercent = Integer.parseInt(parts[0]);
        	 temp = tipPercent +"%";
         }
         tipThisMuch.setText(answer);
         tipPercentEdit.setText(temp);
         calculate();
	}


	
	public void calculate(){

		if(tipPercent >= 100)
			tipPercent = 100;
		if(tipPercent <= 0)
			tipPercent = 0;
		tipPercentEdit.setText(tipPercent+"%");
		String t = billAmountEdit.getText().toString();
		t = t.replaceAll("\\D+", "");
		if(t.equals(""))
			totalAmountEdit.setText("0.00");
		else{
			String tempString = billAmountEdit.getText().toString();
			tempString = tempString.replace(",","");
			double billAmount = Double.parseDouble(tempString);
			double tip =  (double)tipPercent/100;
			double temp = billAmount * tip;
			tipAmount.setText("$"+money.format(temp));
			double total = billAmount + temp;	
			totalAmountEdit.setText("$"+money.format(total));
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.decrement:
			tipPercent = tipPercent - 1;
			calculate();
			break;
		case R.id.increment:
			tipPercent = tipPercent + 1;
			calculate();
			break;
		}
		
	}


	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if(actionId == EditorInfo.IME_ACTION_DONE)
		{
			double billTotal;
			String temp = billAmountEdit.getText().toString();
			temp = temp.replace(",","");
			if(temp.equals(""))
				billTotal = 0;
			else
				billTotal = Double.parseDouble(temp);
			billAmountEdit.setText(money.format(billTotal));
			calculate();
		}
		return false;
	}

}
