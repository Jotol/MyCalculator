package com.mycalculator;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //数字
    private Button num0;
    private Button num1;
    private Button num2;
    private Button num3;
    private Button num4;
    private Button num5;
    private Button num6;
    private Button num7;
    private Button num8;
    private Button num9;

    //运算符
    private Button plus_btn;
    private Button subtract_btn;
    private Button multiply_btn;
    private Button divide_btn;
    private Button equal_btn;

    //其他按钮
    private Button dot_btn;
    private Button percent_btn;
    private Button delete_btn;
    private Button ac_btn;

    //结果
    private EditText tempFormula;
    private TextView resultText;

    //已输入字符
    private String existedText = "";

    private int operatorMaxSize = 20;
    private double[] operand = new double[operatorMaxSize]; //运算数
    private int[] operator = new int[operatorMaxSize];  //运算符
    private double[] tempOperand = new double[operatorMaxSize];//计算过程中的暂时储存的操作数

    private int operatorClass = 0;//1:+, 2:-, 3:*, 4:/, 5:del, 6:ac, 7:=, 8:., 9:%
    private int operandClass = 0; // 1 2 3 4 5 6 7 8 9 0
    private int operatorHead = 0; //记录当前的运算符数量
    private int operandHead = 0; //记录当前的运算数数量

    private int lastCharacter = 0; //记录上一次的运算操作
    private double tempResult = 0; //暂时获得的结果

    private double baseDot = 1;//注意要为double
    private int validDot = 0; //知道有新的符号生效，否则点一直生效
    private int inputSign = 1;//如果第一个字符输入的是符号，则令inputSign = -1

    private int isValidResult = 1;//如果运算符连续，则认为输出无效

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();

    }


    protected void initView() {
        //数字
        num0 = (Button) findViewById(R.id.num_zero);
        num1 = (Button) findViewById(R.id.num_one);
        num2 = (Button) findViewById(R.id.num_two);
        num3 = (Button) findViewById(R.id.num_three);
        num4 = (Button) findViewById(R.id.num_four);
        num5 = (Button) findViewById(R.id.num_five);
        num6 = (Button) findViewById(R.id.num_six);
        num7 = (Button) findViewById(R.id.num_seven);
        num8 = (Button) findViewById(R.id.num_eight);
        num9 = (Button) findViewById(R.id.num_nine);
        //运算符
        plus_btn = (Button) findViewById(R.id.plus_btn);
        subtract_btn = (Button) findViewById(R.id.subtract_btn);
        multiply_btn = (Button) findViewById(R.id.multiply_btn);
        divide_btn = (Button) findViewById(R.id.divide_btn);
        equal_btn = (Button) findViewById(R.id.equal_btn);
        //其他
        dot_btn = (Button) findViewById(R.id.dot_btn);
        percent_btn = (Button) findViewById(R.id.percent_btn);
        delete_btn = (Button) findViewById(R.id.delete_btn);
        ac_btn = (Button) findViewById(R.id.ac_btn);
        //结果
        tempFormula = (EditText) findViewById(R.id.temp_formula);
        resultText = (TextView) findViewById(R.id.temp_result);
        //已输入字符
        existedText = tempFormula.getText().toString();

    }

    private void initEvent() {
        num0.setOnClickListener(this);
        num1.setOnClickListener(this);
        num2.setOnClickListener(this);
        num3.setOnClickListener(this);
        num4.setOnClickListener(this);
        num5.setOnClickListener(this);
        num6.setOnClickListener(this);
        num7.setOnClickListener(this);
        num8.setOnClickListener(this);
        num9.setOnClickListener(this);

        plus_btn.setOnClickListener(this);
        subtract_btn.setOnClickListener(this);
        multiply_btn.setOnClickListener(this);
        divide_btn.setOnClickListener(this);
        equal_btn.setOnClickListener(this);

        dot_btn.setOnClickListener(this);
        percent_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        ac_btn.setOnClickListener(this);

        operand[operandHead] = 0;
    }

    private void allClear() {
        Log.e("In All Clear: ", "Head In");
    }

    //分析获取到的字符串
    private void calculatorAnalyze() {
        Log.e("In Calculator Analyze: ", "Head In");

        int character = 0;

        int startWithSubtract = 1; //首字符不是正号或负号

        operatorHead = 0; //重置当前的运算符数量
        operandHead = 0; //重置当前的运算数数量
        validDot = 0; //重置小数点资源
        baseDot = 1; //重置基数
        lastCharacter = 0; //重置上次的
        isValidResult = 1; //重置有效结果

        for(int i=0;i<existedText.length();i++) {
            //获得字符串每一个字符的含义
            character = detectCharacter(existedText.substring(i,i+1));

            if(lastCharacter > 10 && character > 10){
                Log.e("Error：","连续两次运算符");
                Toast.makeText(MainActivity.this,"error 1",Toast.LENGTH_SHORT).show();
                isValidResult = 0;
            }
            else {
                //用以检测第一个字符是正负号还是数字
                if (operatorHead == 0 && operandHead == 0) {
                    //如果第一个字符是数字
                    if (character < 10) {
                        Log.e("运算数: ", "First Meet");
                        operand[operandHead] = character * startWithSubtract;
                        operandHead = operandHead + 1;
                    }
                    //如果第一个字符是+-*/.
                    else if (character < 16 && character > 10) {
                        Log.e("运算符：", "First Meet");
                        // +
                        if (character == 11) {
                            Log.e("运算符：", "First Meet, startWithPositive");
                            startWithSubtract = 1;
                        }
                        // -
                        else if (character == 12) {
                            Log.e("运算符：", "First Meet, startWithSubtract");
                            startWithSubtract = -1;
                        }
                        // * / .
                        else {
                            Log.e("Error：","错误运算符出现在最前面");
                            Toast.makeText(MainActivity.this, "error 2", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else if (operatorHead != 0 && operandHead == 0){
                    Log.e("Error：","this is impossible happen");
                    Toast.makeText(MainActivity.this, "error 3", Toast.LENGTH_SHORT).show();
                }
                else{
                    //如果当前是数字
                    if (character < 10) {
                        //上一个是数字，则认为是一个数 或者 上一个运算符是 .，则进行小数计数
                        if(lastCharacter < 10 || lastCharacter == 15){
                            Log.e("运算数：","继续计数");
                            if(validDot == 1){
                                Log.e("运算数：","此数是小数");
                                baseDot = baseDot*10;
                                operand[operandHead-1] = operand[operandHead-1] + character/baseDot;
                            }
                            else {
                                Log.e("运算数：","此数是整数");
                                operand[operandHead - 1] = operand[operandHead - 1] * 10 + character;
                            }
                        }
                        //上一个运算符是 +-*/
                        else if(lastCharacter > 10 && lastCharacter < 15){
                            Log.e("运算数：","新增计数");
                            operand[operandHead] = character;
                            operandHead = operandHead + 1;
                        }
                        else{
                            Log.e("Error：","表达式存在错误");
                            Toast.makeText(MainActivity.this, "error 4", Toast.LENGTH_SHORT).show();
                        }

                    }
                    //如果当前是运算符
                    else if(character > 10 && character < 15){
                        Log.e("运算符：","当前是小数点");
                        //释放小数点占用资源
                        if(validDot == 1) {
                            Log.e("运算符：","释放小数点资源");
                            validDot = 0;
                        }
                        //增加运算符
                        operator[operatorHead] = character;
                        operatorHead = operatorHead + 1;
                    }
                    //如果当前是 小数点
                    else if(character == 15){
                        Log.e("小数点：","当前为小数点");
                        if(validDot == 0) {
                            Log.e("小数点：","开启小数点计数模式");
                            validDot = 1;
                            baseDot = 1;
                        }
                        else{
                            Log.e("Error：","小数点出错，未释放");
                            Toast.makeText(MainActivity.this, "error 5", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            lastCharacter = character;
        }

        //输出获取到的数字
        for(int i = 0; i< operandHead;i++) {
            Log.e("Output：operand",Double.toString(operand[i]));
        }
        //输出获取到的运算符
        for(int i = 0; i< operatorHead;i++) {
            Log.e("Output：operator",Double.toString(operator[i]));
        }


        //resultText.setText(Integer.toString(temp));
        Log.e("Out Calculator Analyze：","////////////////////////////////////////////");

    }

    //对解析后的字符串，进行计算
    private double calculatorProcess(){
        double result = 0;
        int isSatisfyCalculator = 0; //为 1 ： 运算数比运算符多1，为 2 ： 运算数与运算符数目相同
        Log.e("In Calulate Progress: ","Head In");
        //暂存运算的数
        for(int i = 0; i< operandHead;i++) {
            tempOperand[i] = operand[i];
        }
        //如果满足设定的条件
        if (operandHead == operatorHead + 1) {
            isSatisfyCalculator = 1;
        }
        //两者相等的时候要注意，不能为空
        else if(operandHead == operatorHead && operandHead != 0) {
            isSatisfyCalculator = 2;
        }
        if(isSatisfyCalculator != 0){
            Log.e("In Calulate Progress: ",",Meet Condition, Start Process...");
            int j = 0;
            for(int i = 0; i< operatorHead + 1 - isSatisfyCalculator;i++){
                //先做乘除法
                //eg. B * C = E,
                //  A + B * C + D ---> A + E + D
                // 将 B 、 C 都赋成 E
                if(operator[i] == 13){
                    tempOperand[i+1] = tempOperand[i] * tempOperand[i+1];
                    Log.e("In Calulate:Multiply ->",Double.toString(tempOperand[i+1]));
                    j = i;
                    //向左传递计算结果
                    while((operator[j] == 13 || operator[j] == 14) && j>=0) {
                        tempOperand[j] = tempOperand[j + 1];
                        j--;
                        if(j==-1) break;
                    }
                }
                else if(operator[i] == 14){
                    tempOperand[i+1] = tempOperand[i] / tempOperand[i+1];
                    Log.e("In Calulate:Divide ->",Double.toString(tempOperand[i+1]));
                    j = i;
                    while((operator[j] == 13 || operator[j] == 14) && j>=0) {
                        tempOperand[j] = tempOperand[j + 1];
                        j--;
                        if(j==-1) break;
                    }
                }
            }

            //做加减运算
            result = tempOperand[0];
            for(int i = 0; i< operatorHead;i++){
                if(operator[i] == 11){
                    result = result + tempOperand[i + 1];
                }
                else if(operator[i] == 12){
                    result = result - tempOperand[i + 1];
                }
            }
            Log.e("In Calulate：Result->",Double.toString(result));
        }

        Log.e("Out Calulate Process","/////////////////////////////////");
        return  result;
    }
    //用于识别字符，分为运算符和运算数
    //0--9 数字，11:+, 12:-, 13:*, 14:/, 15:.
    private int detectCharacter(String s) {
        if(s.equals("0"))
            return 0;
        else if(s.equals("1"))
            return 1;
        else if(s.equals("2"))
            return 2;
        else if(s.equals("3"))
            return 3;
        else if(s.equals("4"))
            return 4;
        else if(s.equals("5"))
            return 5;
        else if(s.equals("6"))
            return 6;
        else if(s.equals("7"))
            return 7;
        else if(s.equals("8"))
            return 8;
        else if(s.equals("9"))
            return 9;
        else if(s.equals("+"))
            return 11;
        else if(s.equals("-"))
            return 12;
        else if(s.equals("*"))
            return 13;
        else if(s.equals("/"))
            return 14;
        else if(s.equals("."))
            return 15;
        else return 10;
    }

    private void deleteProcess(){
        Log.e("In Delete Process","Head In");
        if(existedText.length()!=0) {
            existedText = existedText.substring(0, existedText.length() - 1);
        }
        Log.e("Out Delete Process","/////////////////////////////////");
    }


    public void onClick(View v) {
        double result = 0;
        operatorClass = 0;//每次初始化为0

        switch (v.getId()) {
            //数字
            case R.id.num_zero:
                operandClass = 0;
                existedText += "0";
                break;
            case R.id.num_one:
                operandClass = 1;
                existedText += "1";
                break;
            case R.id.num_two:
                operandClass = 2;
                existedText += "2";
                break;
            case R.id.num_three:
                operandClass = 3;
                existedText += "3";
                break;
            case R.id.num_four:
                operandClass = 4;
                existedText += "4";
                break;
            case R.id.num_five:
                operandClass = 5;
                existedText += "5";
                break;
            case R.id.num_six:
                operandClass = 6;
                existedText += "6";
                break;
            case R.id.num_seven:
                operandClass = 7;
                existedText += "7";
                break;
            case R.id.num_eight:
                operandClass = 8;
                existedText += "8";
                break;
            case R.id.num_nine:
                operandClass = 9;
                existedText += "9";
                break;
            //运算符   1:+, 2:-, 3:*, 4:/, 5:del, 6:ac, 7:=, 8:., 9:%
            case R.id.plus_btn:
                existedText += "+";
                operatorClass = 1;
                break;
            case R.id.subtract_btn:
                existedText += "-";
                operatorClass = 2;
                break;
            case R.id.multiply_btn:
                existedText += "*";
                operatorClass = 3;
                break;
            case R.id.divide_btn:
                existedText += "/";
                operatorClass = 4;
                break;
            case R.id.delete_btn:
                deleteProcess();
                operatorClass = 5;
                break;
            case R.id.ac_btn:
                existedText = "";
                operatorClass = 6;
                break;
            case R.id.equal_btn:
                operatorClass = 7;
                break;
            case R.id.dot_btn:
                existedText += ".";
                operatorClass = 8;
                break;
            case R.id.percent_btn:
                operatorClass = 9;
                break;
            default:
                break;
        }


        //对输入的字符串进行分析
        calculatorAnalyze();
        result = calculatorProcess();
        //时时显示结果
        if(isValidResult == 1){
            resultText.setText(Double.toString(result));
        }
        else{
            resultText.setText("error");
        }

        //如果按下了 = 按钮，则将结果也显示在输入区
        if(operatorClass == 7){
            if(isValidResult == 1){
                existedText = Double.toString(result);
            }
            else{
                existedText = "";
            }

        }
        //更新当前的输入区
        tempFormula.setText(existedText);
        tempFormula.setSelection(tempFormula.getText().length());//光标移至最右边
    }
}