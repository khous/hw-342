import java.awt.*;
import javax.swing.*;

/**
 * A polynomial calculator
 * @author TCSS 342
 * @version 1.0
 */

public class PolyCalc {

    private PolyCalcEngine engine;
    private PolyCalcUI gui;

    public PolyCalc() {
        //Construct the application
        engine = new PolyCalcEngine();
        gui = new PolyCalcUI(engine);
        JFrame frame = gui.getFrame();

        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
        gui.redisplay();
    }

    public static void main(String[] args) {
        test();
//        new PolyCalc();
    }


    public static void test () {
        //Test empty polynomial
        Polynomial a = new Polynomial();

        assert a.print().equals("0");

        //test term insertion
          //test adding unique terms
        a.insertTerm(1, 0);
        a.insertTerm(1, 1);
        a.insertTerm(1, 2);
        assert a.print().equals("x^2 + x + 1");
          //test adding same order terms
        a.insertTerm(1,2);
        assert a.print().equals("2x^2 + x + 1");
        //test adding terms which cancel a term to zero
        a.insertTerm(-1, 1);
        assert a.print().equals("2x^2 + 1");
        //test adding a term to beginning
        a.insertTerm(1, 3);
        assert a.print().equals("x^3 + 2x^2 + 1");
        //test adding a term to middle
        a.insertTerm(1, 1);
        assert a.print().equals("x^3 + 2x^2 + x + 1");
        //test adding a term to the end
        a.insertTerm(2, 0);
        assert a.print().equals("x^3 + 2x^2 + x + 3");


        //test addition
        Polynomial b = new Polynomial();
            //test adding unique terms
        b.insertTerm(1, 4);
        b.insertTerm(1, 5);

        assert a.plus(b).print().equals("x^5 + x^4 + x^3 + 2x^2 + x + 3");

        //test adding same order terms
        b.zeroPolynomial();
        b.insertTerm(1,3);
        assert a.plus(b).print().equals("2x^3 + 2x^2 + x + 3");
        //test adding terms which cancel a term to zero

        b.zeroPolynomial();
        b.insertTerm(-2, 2);
        assert a.plus(b).print().equals("x^3 + x + 3");

        //test adding a term to middle
        b.zeroPolynomial();
        b.insertTerm(1, 2);
        assert a.plus(b).print().equals("x^3 + 3x^2 + x + 3");
        //test adding a term to the end
        b.zeroPolynomial();
        b.insertTerm(-3, 0);
        assert a.plus(b).print().equals("x^3 + 2x^2 + x");

        b.zeroPolynomial();
        //test subtraction
        //a = x^3 + 2x^2 + x + 3
        assert a.minus(a).print().equals("0");
          //test subtracting terms which aren't in the polynomial
        b.insertTerm(1, 4);
        assert a.minus(b).print().equals("-x^4 + x^3 + 2x^2 + x + 3");
        b.zeroPolynomial();
        b.insertTerm(1, -4);
        assert a.minus(b).print().equals("x^3 + 2x^2 + x + 3 - x^(-4)");

        b.zeroPolynomial();
        b.insertTerm(1, 3);
        b.insertTerm(2, 2);
        //test subtracting highest order term
        assert a.minus(b).print().equals("x + 3");
      //test subtracting lowest order term
        b.zeroPolynomial();

        //test negation
        assert a.negate().print().equals("-x^3 - 2x^2 - x - 3");
          //test negation
        b.zeroPolynomial();
//        assert b.negate().equals("0");for some reason zero does not equal zero right now

        //test derivative
        a.zeroPolynomial();
        a.insertTerm(1, 3);
        a.insertTerm(1, 2);
        a.insertTerm(1, 1);
        a.insertTerm(1, 0);

        assert a.derivative().print().equals("3x^2 + 2x + 1");
          //test on poly with no terms
        b.zeroPolynomial();
        assert b.derivative().print().equals("0");
        //test on poly with only constant
        b.insertTerm(1, 0);
        assert b.derivative().print().equals("0");

        //test times
          //test on empty polynomial
        a.zeroPolynomial();
        a.insertTerm(1, 3);
        a.insertTerm(1, 2);
        a.insertTerm(1, 1);
        a.insertTerm(1, 0);
        assert a.times(a).print().equals("x^6 + 2x^5 + 3x^4 + 4x^3 + 3x^2 + 2x + 1");

        //test negate more
        //test with single literal
        //test with single term


        System.out.println("Done");
    }
}