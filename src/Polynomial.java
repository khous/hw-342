import java.util.Iterator;
import java.util.Stack;

/**
 * This class provides stores a polynomial and allows math operation with the stored polynomial including
 * mulitplication, addition, subtraction and derivation.
 */
public class Polynomial {

    LinkedList myPolynomial;

    public Polynomial () {
        myPolynomial = new LinkedList();
    }

    /**
     * Return the string representation of this polynomial
     * @return this polynomial as a string
     */
    public String print () {
        //Return zero if the polynomial has no terms
        if (myPolynomial.isEmpty()) {
            return "0";
        }

        StringBuilder output = new StringBuilder();
        boolean firstTerm = true;//Track if this term is the first one, to handle special formatting
        LinkedList.Iterator iter = myPolynomial.iterator();

        while (iter.hasNext()) {
            Literal term = (Literal) iter.next();
            //Add the output of the printing helper method
            output.append(literalToStringBuilder(term, firstTerm));
            firstTerm = false;
        }

        return output.toString();
    }

    /**
     * This method print one literal considering its position in the larger polynomial.
     * The method prepends the operator for this term based on its position in the expression.
     * @param term
     * @param firstTerm
     * @return
     */
    private StringBuilder literalToStringBuilder (Literal term, boolean firstTerm) {
        StringBuilder s = new StringBuilder();
        String x = "x";

        int coefficient = term.getCoefficient(),
            exponent = term.getExponent();

        //TODO Break this down further
        //ADDITION
        //If this term is positive and not the first term, indicate addition
        if (term.getCoefficient() > 0 && !firstTerm){
            s.insert(0, " + ");//prepend operator to the front
        }

        if (exponent == 0) {
            if (coefficient > 0) {
                s.append(coefficient);
            } else {
                String sign = firstTerm ? "-" : " - ";
                s.append(sign).append(Math.abs(coefficient));
            }
            return s;
        }

        //COEFFICIENT positive/negative, or 1
        //Otherwise there is an exponent so add X
        if (coefficient > 1) {//Handle positive numbers greater than 1, so we don't end up with 1x
            s.append(coefficient).append(x);
        } else if (coefficient < 0) {//Handle negatives, add a negation if this is the first term, else a speced operand
            s.append(getSignedCoefficient(coefficient, firstTerm));
            s.append(x);
        } else {//Otherwise, if the coefficient is 1, just add "x"
            s.append(x);
        }

        //EXPONENT
        //Handle the exponent
        if (exponent > 1) {
            s.append("^").append(exponent);
        } else if (exponent < 0) {
            s.append("^(").append(exponent).append(")");
        }

        return s;
    }

    private StringBuilder getSignedCoefficient (int coefficient, boolean firstTerm) {
        String sign = firstTerm ? "-" : " - ";
        StringBuilder s = new StringBuilder();
        s.append(sign);
        if (coefficient < -1) {
            s.append(Math.abs(coefficient));
        }

        return s;
    }

    /**
     * Calculate and return first derivative
     * @return The first derivative of this polynomial
     */
    public Polynomial derivative () {
        Polynomial outputCopy = new Polynomial();
        LinkedList.Iterator iter = myPolynomial.iterator();

        while (iter.hasNext()) {
            Literal term = (Literal) iter.next();

            //Delete this term by omission
            if (term.getExponent() == 0) {
                continue;
            }

            //Do the derivation by multiplying the cofficient by the exponent, and decrementing the exponent
            outputCopy.insertTerm(term.getCoefficient() * term.getExponent(), term.getExponent() - 1);
        }

        return outputCopy;
    }

    /**
     * Multiply two polynomials and return the result
     * @param factor The other polynomial with which to mulitply this one by
     * @return The result of the multiplication
     */
    public Polynomial times (Polynomial factor) {
        Polynomial outputCopy = new Polynomial();

        LinkedList.Iterator iterator = myPolynomial.iterator();

        while (iterator.hasNext()) {
            //Multiply this term by each term in the factor
            Literal term = (Literal) iterator.next();
            LinkedList.Iterator factorIterator = factor.myPolynomial.iterator();

            while (factorIterator.hasNext()) {
                Literal factorTerm = (Literal) factorIterator.next();

                //Multiply the coeffiecients and add the exponents
                outputCopy.insertTerm(term.getCoefficient() * factorTerm.getCoefficient(),
                        term.getExponent() + factorTerm.getExponent());
            }
        }

        return outputCopy;
    }

    /**
     * Perform subtraction on this polynomial
     * @param minuend The polynomial to subtract from this polynomial
     * @return The result of the subtraction
     */
    public Polynomial minus (Polynomial minuend) {
        //Negate minuend and call plus, same as this + (-minuend)
        Polynomial negationOfMinuend = minuend.negate();
        return plus(negationOfMinuend);
    }

    /**
     * Get the negation of this polynomial
     * @return the negated form of this polynomial
     */
    public Polynomial negate () {
        Stack<Literal> terms = new Stack<>();
        Polynomial outputCopy = new Polynomial();

        LinkedList.Iterator iter = myPolynomial.iterator();

        while (iter.hasNext()) {
            Literal term = (Literal) iter.next();
            //Push negated terms onto a stack
            terms.push(new Literal(-term.getCoefficient(), term.getExponent()));
        }

        //Since the order out of a stack in reversed, each insertion will be done in constant time
        for (Literal term : terms) {
            outputCopy.insertTerm(term.getCoefficient(), term.getExponent());
        }

        return outputCopy;
    }

    /**
     * Perform addition on this polynomial with another polynomial
     * @param addend The polynomial to add to this one
     * @return The sum of the two polynomials
     */
    public Polynomial plus (Polynomial addend) {
        //Perform a sorted list merge in O(n + m) time. None of this n^2 garbage
        Polynomial outputCopy = clone();//Create the clone that will be used for output

        //Initialize iterators, start the out copy at the zeroth position to perform lookahead
        LinkedList.Iterator iter = outputCopy.myPolynomial.zeroth(),
            addendIterator = addend.myPolynomial.iterator();

        //There are no nodes in this polynomial, just return the addend
        if (iter.getNode().getNext() == null) {
            return addend.clone();
        }

        Literal addendTerm = safeNext(addendIterator);

        //hasNext wasn't really sufficient for iterating through the addend,
        while (addendTerm != null && iter.hasNext()) {
            ListNode nextNode = iter.getNode().getNext();

            //If the next node is null, we can just add a new one on the end with a term from the addend
            if (nextNode == null) {
                //set next to new node with current addend term, advance addend iter
                ListNode newNode = new ListNode(addendTerm);
                iter.getNode().setNext(newNode);
                addendTerm = safeNext(addendIterator);
                continue;
            }

            Literal nextTerm = (Literal) nextNode.getElement();

            int addendExponent = addendTerm.getExponent(),
                nextExponent = nextTerm.getExponent();

            //If the addend term is a higher order than the next term, insert term here, move on.
            if (addendExponent > nextExponent) {
                outputCopy.myPolynomial.insert(addendTerm, iter);
                addendTerm = safeNext(addendIterator);
            } else if (addendExponent == nextExponent) {//If they're equal, just add the literals.
                addTermsInPlace(addendTerm, nextTerm, nextNode, outputCopy, iter);
                addendTerm = safeNext(addendIterator);//move to next addend term
                continue;//don't grab iter next in this case. Remove advances for us
            }

            //Step past another node every time
            iter.next();
        }

        return outputCopy;
    }

    /**
     * Special method for adding terms in their place in a linked list. Handles removal in the case that the terms cancel
     * @param addendA The first term to add
     * @param addendB The second term to add
     * @param nextNode The next node in the linked list
     * @param output The output polynomial to modify
     * @param iter The iterator of the output polynomial
     */
    private void addTermsInPlace (Literal addendA, Literal addendB, ListNode nextNode,
                                  Polynomial output, LinkedList.Iterator iter) {
        Literal sum = addLiterals(addendA, addendB);

        if (sum.getCoefficient() != 0) {//Add in place
            nextNode.setElement(sum);
            iter.next();
        } else {//terms cancel, so remove the term and move on
            output.myPolynomial.remove(iter);
        }
    }

    /**
     * Return the next term in an iterator or null
     * @param iterator the iterator from which to grab the next term
     * @return The next literal in the iterator or null
     */
    private Literal safeNext (LinkedList.Iterator iterator) {
        return iterator.hasNext() ? (Literal) iterator.next() : null;
    }

    /**
     * Insert a term into this polynomial
     * @param coefficient The coeffiecient for the new term
     * @param exponent The exponent of the new term
     */
    public void insertTerm (int coefficient, int exponent) {
        //Do not add zeroes to this polynomial ever
        if (coefficient == 0)
            return;

        //if the head node has no next element, just add this after head
        LinkedList.Iterator iter = myPolynomial.zeroth();
        Literal newTerm = new Literal(coefficient, exponent);

        if (iter.getNode().getNext() == null) {
            myPolynomial.insert(newTerm, iter);
            return;
        }

        while (iter.hasNext()) {
            ListNode nextNode = iter.getNode().getNext();

            if (nextNode == null) {//There is no next to compare with, insert to the end
                break;
            }

            Literal nextTerm = (Literal) nextNode.getElement();

            if (newTerm.getExponent() > nextTerm.getExponent()) {
                myPolynomial.insert(newTerm, iter);
                return;
            } else if (newTerm.getExponent() == nextTerm.getExponent()) {
                Literal sum = addLiterals(newTerm, nextTerm);
                addTermsInPlace(newTerm, nextTerm, nextNode,this, iter);
                return;
            }

            iter.next();
        }

        myPolynomial.insert(newTerm, iter);
    }

    /**
     * Add two literals together.
     * @param addendA The first term to add
     * @param addendB The second term to add
     * @return The sum of the two literals
     */
    private Literal addLiterals (Literal addendA, Literal addendB) {
        return new Literal(addendA.getCoefficient() + addendB.getCoefficient(), addendA.getExponent());
    }

    /**
     * Set the polynomial back to zero
     */
    public void zeroPolynomial () {
        myPolynomial.makeEmpty();
    }

    /**
     * Return a deep copy of this polynomial
     * @return A deep copy of this polynomial
     */
    public Polynomial clone () {
        Polynomial clone = new Polynomial();
        LinkedList.Iterator iter = myPolynomial.iterator();

        while (iter.hasNext()) {
            Literal term = (Literal) iter.next();
            clone.insertTerm(term.getCoefficient(), term.getExponent());
        }

        return clone;
    }
}
