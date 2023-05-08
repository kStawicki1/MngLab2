import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CalculatorMainListener extends CalculatorBaseListener {
    Deque<Double> numbers = new ArrayDeque<>();

    private Double getResult() {
        return numbers.peek();
    }
    @Override
    public void exitExpression(CalculatorParser.ExpressionContext ctx) {
        double value = numbers.pop();
        for (int i = 1; i < ctx.getChildCount(); i +=2) {
            if(ctx.getChild(i).getText().equals("+")) {
                value += numbers.pop();
            } else if(ctx.getChild(i).getText().equals("-")) {
                value -= numbers.pop();
            }

        }
        numbers.add(value);
        super.exitExpression(ctx);
    }

    @Override
    public void exitMultiplyingExpression(CalculatorParser.MultiplyingExpressionContext ctx) {
        if (ctx.DIV().size() != 0 || ctx.MULT().size() != 0) {
            double result;
            List<Double> tempList = getNumbersFromQueue(numbers.size() - 2);
            if(ctx.DIV().size() != 0) {
                result = numbers.pop() / numbers.pop();
            } else {
                result = numbers.pop() * numbers.pop();
            }
            populateQueue(tempList);
            numbers.add(result);
        }
        super.exitMultiplyingExpression(ctx);
    }

    @Override
    public void exitIntegralExpression(CalculatorParser.IntegralExpressionContext ctx) {
        if(ctx.MINUS() != null) {
            List<Double> tempList = getNumbersFromQueue(numbers.size() - 1);
            double result = -1 * numbers.pop();
            populateQueue(tempList);
            numbers.add(result);
        }
        super.exitIntegralExpression(ctx);
    }

    @Override
    public void exitPowExpression(CalculatorParser.PowExpressionContext ctx) {
        if (ctx.POW().size() != 0) {
            Double result;
            List<Double> tempList = getNumbersFromQueue(numbers.size() - 1);
            if(ctx.multiplyingExpression().size() != 0) {
                 result = Math.pow(Double.valueOf(ctx.INT(0).toString()), numbers.pop());
            } else {
                result = Math.pow(Double.valueOf(ctx.INT(0).toString()),Double.valueOf(ctx.INT(1).toString()));
            }
            populateQueue(tempList);
            numbers.add(result);
        } else {
            numbers.add(Double.valueOf(ctx.INT(0).toString()));
        }
        super.exitPowExpression(ctx);
    }

    public static void main(String[] args) throws Exception {

        System.out.println("Result = " + calc("3 + 2 - 1)"));
        System.out.println("Result = " + calc("3 + -3 + 2)"));
        System.out.println("Result = " + calc("3 * -3 + 2 )"));
        System.out.println("Result = " + calc("3^2 + 2"));
        System.out.println("Result = " + calc("4^(1/2) + -2*4*2 + -2/2"));
    }

    public static Double calc(String expression) {
        return calc(CharStreams.fromString(expression));
    }

    public static Double calc(CharStream charStream) {
        CalculatorLexer lexer = new CalculatorLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree tree = parser.expression();

        ParseTreeWalker walker = new ParseTreeWalker();
        CalculatorMainListener mainListener = new CalculatorMainListener();
        walker.walk(mainListener, tree);
        return mainListener.getResult();
    }

    private List<Double> getNumbersFromQueue(int amount) {
        List<Double> tempList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            tempList.add(numbers.pop());
        }
        return tempList;
    }

    private void populateQueue(List<Double> listOfEntries) {
        listOfEntries.forEach(numbers::add);
    }
}
