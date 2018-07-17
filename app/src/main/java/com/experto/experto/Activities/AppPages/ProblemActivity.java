package com.experto.experto.Activities.AppPages;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.experto.experto.AppData.Device;
import com.experto.experto.AppData.Problem;
import com.experto.experto.ListItems.ProblemItem;
import com.experto.experto.Adapters.ProblemItemsAdapter;
import com.experto.experto.R;
import com.experto.experto.AppData.ServiceA;
import com.experto.experto.AppData.ServiceB;

import java.util.ArrayList;
import java.util.List;

public class ProblemActivity extends AppCompatActivity {

    private ProblemItemsAdapter adapter;
    private List<ProblemItem> problemItems;
    private GridView problemItemsList;
    private ArrayList<Problem> problemList;
    private Button checkOut;
    public static List<Integer> problemsIndexes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        problemItemsList = (GridView) findViewById(R.id.problem_grid);
        problemsIndexes = new ArrayList<>();
        checkOut = (Button) findViewById(R.id.check_out);
        problemItems = new ArrayList<ProblemItem>();
        adapter = new ProblemItemsAdapter(this, R.layout.problem_item, problemItems);
        if (Home.getRequestInfo().get("service").getClass().equals(ServiceA.class)) {
            problemList = (ArrayList)((Device) Home.getRequestInfo().get("device")).getProblems();
        } else if (Home.getRequestInfo().get("service").getClass().equals(ServiceB.class)) {
            problemList =  (ArrayList)((ServiceB) Home.getRequestInfo().get("service")).getProblems();
        }
        if (problemList != null) {
            for (int i = 0; i < problemList.size(); i++) {
                Problem problem = problemList.get(i);
                String name = problem.getName();
                int price = problem.getServiceFee();
                // get the resource of the image
                String nameLowerCase = "mobile";
                int resource = getResources().getIdentifier(nameLowerCase, "drawable", getPackageName());
                Drawable drawable;
                if(resource !=0){
                    drawable = getResources().getDrawable(resource);
                }
                else {
                    drawable = null;
                }

                ProblemItem problemItem = new ProblemItem(name, price,drawable);
                adapter.add(problemItem);
            }
        }
        else {
            Toast.makeText(this,R.string.noData,Toast.LENGTH_LONG).show();
        }
        problemItemsList.setAdapter(adapter);
        problemItemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapter.getProblemsIndexes().contains((Integer) i)){
                    // to record the indexes of the selected problems and to make them transparent
                    adapter.getProblemsIndexes().add((Integer)i);
                    adapter.notifyDataSetChanged();

                }
                else {
                    // to remove the indexes of the unselected problems and to make them not transparent
                    adapter.getProblemsIndexes().remove((Integer)i);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(adapter.getProblemsIndexes().size()==0){
                   Toast.makeText(ProblemActivity.this,"Please select al least one problem",Toast.LENGTH_SHORT).show();
               }
               else {
                   Home.getRequestInfo().put("problemsIndexes", adapter.getProblemsIndexes());
                   Home.getRequestInfo().put("problems", problemList);
                   Intent intent = new Intent(ProblemActivity.this,RequestActivity.class);
                   startActivity(intent);
               }
            }
        });
    }
}
