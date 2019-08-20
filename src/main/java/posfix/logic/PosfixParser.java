package posfix.logic;

/**
 * @program: expression
 * @author: YangAidi
 * @create: 2019-08-20 23:04
 **/

import java.util.Stack;

/// <summary>
/// 将中缀表达式翻译成后缀表达式
/// 输入中缀表达式： A+B*(C+D)-E/F
/// 翻译成后缀表达式：ABCD+*+EF/-
/// 中缀表达式翻译成后缀表达式的方法如下：
/// （1）从左向右依次取得数据ch
/// （2）如果ch是操作数，直接输出
/// （3）如果ch是运算符（含左右括号），则：
///                a：如果ch = '('，放入堆栈
///                b：如果ch = ')'，依次输出堆栈中的运算符，直到遇到'('为止
///                c：如果ch不是')'或者'('，那么就和堆栈顶点位置的运算符top做优先级比较
///                        1：如果ch优先级比top高，那么将ch放入堆栈
///                        2：如果ch优先级低于或者等于top，那么输出top，然后将ch放入堆栈
///    （4）如果表达式已经读取完成，而堆栈中还有运算符时，依次由顶端输出
    /*        Pseudocode()
            {
                n = passing(s, op); //s是表达式，op是数据数组，n是数据的数量

                for(int i=0; i<n; i++)
                {
                    ch = op(i);
                    if(ch是操作数)
                        output(ch);
                    else
                    {
                        if(ch == '(')
                            push(ch);
                        else if( ch == ')')
                            pop()而且输出，直到遇到'('为止;
                        else
                        {
                            if(运算符ch较stack[top]优先)
                                 push(ch);
                            else
                            {
                                pop()且输出;
                                push(ch);
                            }
                        }
                    }
                }
    */
/// </summary>
public class PosfixParser {
    private static String expression = "(a&b)|(a|b)";
    private static Stack<Character> myStack = new Stack<>();
    private static Stack<Boolean> resultStack = new Stack<>();
    private static StringBuilder posfixExpression = new StringBuilder();

    public static void main(String[] args) {
        System.out.println("This Midfix expression is: " + expression);
        System.out.println("The Posfix expression is: " + parse());
        System.out.println("The result is " + calculate());
        /*
         输出结果如下：
         - This Midfix expression is: (a&b)|(a|b)
         - The Posfix expression is: ab&ab||
         - The result is true
        */
    }

    //将中缀表达式解析成后缀表达式
    public static String parse() {
        int i, j = 0;
        char ch, ch1;
        //将字符串转成字符数组，要注意的是，不能有大于10的数存在
        char[] a = expression.toCharArray();
        //最后生成的后缀表达式会小于这个长度，因为有括号
        char[] b = new char[a.length];
        int length = a.length;

        for (i = 0; i < length; i++) {
            ch = a[i];
            //如果是操作数，直接放入B中
            if (isOperand(ch)) {
                b[j++] = ch;
            } else {
                //如果是'('，将它放入堆栈中
                if (ch == '(') {
                    myStack.push(ch);
                } else if (ch == ')') {
                    //如果是')',不停地弹出堆栈中的内容，直到遇到'('
                    while (!myStack.isEmpty()) {
                        ch = myStack.pop();
                        if (ch == '(') {
                            break;
                        } else {
                            //将堆栈中弹出的内容放入B中
                            b[j++] = ch;
                        }
                    }
                } else //既不是'('，也不是')'，是其它操作符，比如+, -, *, /之类的
                {
                    if (!myStack.isEmpty()) {
                        do {
                            //弹出栈顶元素
                            ch1 = myStack.pop();
                            //如果栈顶元素的优先级小于读取到的操作符
                            if (priority(ch) > priority(ch1)) {
                                //将栈顶元素放回堆栈
                                myStack.push(ch1);
                                //将读取到的操作符放回堆栈
                                myStack.push(ch);
                                break;
                            } else//如果栈顶元素的优先级比较高或者两者相等时
                            {
                                //将栈顶元素弹出，放入B中
                                b[j++] = ch1;
                                if (myStack.isEmpty()) {
                                    //将读取到的操作符压入堆栈中
                                    myStack.push(ch);
                                    break;
                                }
                            }
                        } while (true);
                    } else //如果堆栈为空，就把操作符放入堆栈中
                    {
                        myStack.push(ch);
                    }
                }
            }
        }

        while (!myStack.isEmpty()) {
            //将堆栈中剩下的操作符输出到B中
            b[j++] = myStack.pop();
        }

        for (i = 0; i < b.length; i++) {
            //去除多余的空字符
            if (b[i] != '\0') {
                posfixExpression.append(b[i]);
            }
        }

        return posfixExpression.toString();
    }

    //计算后缀表达式的值
    public static boolean calculate() {
        int i;
        boolean no1, no2, ret;
        char ch;
        char[] a = posfixExpression.toString().toCharArray();

        resultStack.clear();

        for (i = 0; i < a.length; i++) {
            ch = a[i];
            //如果是操作数，直接压入栈
            if (isOperand(ch)) {
                // -----------todo 此处获取a或者b或者其他操作数的真值------------
                resultStack.push(ch == 'a');
            } else //如果是操作符，就弹出两个数字来进行运算
            {
                no1 = resultStack.pop();
                no2 = resultStack.pop();
                ret = getValue(ch, no1, no2);
                //将结果压入栈
                resultStack.push(ret);
            }
        }

        //弹出最后的运算结果
        return resultStack.pop();
    }

    //对两个值利用运算符计算结果
    private static boolean getValue(char op, boolean ch1, boolean ch2) {
        switch (op) {
            case '|':
                return ch2 || ch1;
            case '&':
                return ch2 && ch1;
            default:
                return false;
        }
    }

    //判断是否是操作数
    private static boolean isOperand(char ch) {
        char[] operators = {'|', '&', '(', ')'};
        for (char operator : operators) {
            if (ch == operator) {
                return false;
            }
        }

        return true;
    }

    //返回运算符的优先级
    private static int priority(char ch) {
        int priority;

        switch (ch) {
            case '|':
            case '&':
                priority = 1;
                break;
            default:
                priority = 0;
                break;
        }

        return priority;
    }
}