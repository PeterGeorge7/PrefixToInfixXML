import java.io.IOException;
import org.xml.sax.SAXException;

import Exceptions.MissingOperand;
import Exceptions.MissingOperator;
import Exceptions.NotValidOperatorException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class to convert prefix expressions to infix expressions and perform
 * calculations from XML.
 */
public class PrefixToInfix {
    // Instance variables
    private int numberOfElements;
    private String prefixExpression;
    private StaticStack<String> prefixExpressionStack;
    private String infixExpression;
    private StaticStack<String> infixExpressionStack;
    private Double result;
    private StaticStack<Double> calculatorStack;

    /**
     * Default constructor initializes instance variables.
     */
    public PrefixToInfix() {
        this.prefixExpression = "";
        this.infixExpression = "";
        this.numberOfElements = 0;
        this.result = 0d;
    }

    /**
     * Getter for the prefix expression.
     * 
     * @return Prefix expression.
     */
    public String getPrefixExpression() {
        return (this.prefixExpression);
    }

    /**
     * Getter for the infix expression.
     * 
     * @return Infix expression.
     */
    public String getInfixExpression() {
        return (this.infixExpression);
    }

    /**
     * Validates an XML file and returns a Document object.
     * 
     * @param xmlPath Path to the XML file.
     * @return Parsed Document object or null if an error occurs.
     */
    public Document checkXmlValid(String xmlPath) {
        Document parsedXml = null;
        try {
            // Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse the XML file and obtain the document
            parsedXml = builder.parse(xmlPath);
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfigurationException: " + e.getMessage());
            return null;
        } catch (SAXException e) {
            System.out.println("SAXException: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            return null;
        }
        return parsedXml;
    }

    /**
     * Initializes the size of the stacks based on the number of elements in the
     * XML.
     * 
     * @param element XML element to process.
     * @return Initialized prefix expression stack.
     */
    public StaticStack<String> intiallizeStackSize(Element element) {
        this.numberOfElements = count(element);
        this.prefixExpressionStack = new StaticStack<>(numberOfElements);
        this.infixExpressionStack = new StaticStack<>(numberOfElements);
        return this.prefixExpressionStack;
    }

    /**
     * Recursively counts the number of elements in the XML.
     * 
     * @param element XML element to process.
     * @return Number of elements.
     */
    private int count(Element element) {
        NamedNodeMap attributes = element.getAttributes();

        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            // Increment the count for each attribute with the specified conditions
            if ((element.getNodeName().equals("operator") || element.getNodeName().equals("atom"))
                    && (attribute.getNodeName().equals("value"))) {
                this.numberOfElements++;
            }
        }

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                count((Element) node);
            }
        }
        return this.numberOfElements;
    }

    /**
     * Reads attributes from an XML element and updates the prefix expression stack.
     * 
     * @param element XML element to process.
     */
    public void readAttributes(Element element) {

        NamedNodeMap attributes = element.getAttributes();

        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            // Push attribute value to the stack and update the prefix expression
            if ((element.getNodeName().equals("operator") || element.getNodeName().equals("atom"))
                    && (attribute.getNodeName().equals("value"))) {
                prefixExpressionStack.push(attribute.getNodeValue());
                updatePrefixExpression(attribute.getNodeValue());
            }
        }

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                readAttributes((Element) node); // Recursively process child elements
            }
        }
    }

    /**
     * Updates the prefix expression with a new value.
     * 
     * @param newPrefixExpression New value to append to the prefix expression.
     */
    public void updatePrefixExpression(String newPrefixExpression) {
        this.prefixExpression = this.prefixExpression + " " + newPrefixExpression;
    }

    /**
     * Calculates the result of the infix expression from the XML using recursion.
     * 
     * @param xmlPath Path to the XML file.
     */
    public void calcInfixExpressionResultFromXml(String xmlPath) {
        Document xmlDocument = checkXmlValid(xmlPath);
        if (xmlDocument == null) {
            return;
        }

        // Initialize stack sizes and read attributes from the XML
        prefixExpressionStack = intiallizeStackSize(xmlDocument.getDocumentElement());
        readAttributes(xmlDocument.getDocumentElement());
        this.calculatorStack = new StaticStack<Double>(numberOfElements);

        // Start the recursive calculation
        processInfixExpressionCalculation(numberOfElements - 1);

        this.result = this.calculatorStack.pop();
        this.infixExpression = this.infixExpressionStack.pop();
        printResults();
    }

    /**
     * Recursively calculates the result of the infix expression.
     * 
     * @param index Index of the current element in the prefix expression.
     */
    private void processInfixExpressionCalculation(int index) {
        if (index < 0) {
            return;
        }

        String c = this.prefixExpressionStack.pop();

        if (isNumeric(c)) {
            this.calculatorStack.push(Double.parseDouble(c));
            this.infixExpressionStack.push(c);
        } else {

            if (calculatorStack.stackSize() < 2) {
                try {
                    throw new MissingOperand();
                } catch (MissingOperand e) {

                    System.out.println(e.getMessage());

                    System.exit(0);
                }
            }

            // Pop to calculate result
            Double operand1 = this.calculatorStack.pop();
            Double operand2 = this.calculatorStack.pop();
            // Pop to create infix expression
            String c1 = this.infixExpressionStack.pop();
            String c2 = this.infixExpressionStack.pop();

            switch (c) {
                case "+":
                    this.calculatorStack.push(operand1 + operand2);
                    this.infixExpressionStack.push("(" + c1 + " + " + c2 + ")");
                    break;
                case "-":
                    this.calculatorStack.push(operand1 - operand2);
                    this.infixExpressionStack.push("(" + c1 + " - " + c2 + ")");
                    break;
                case "*":
                    this.calculatorStack.push(operand1 * operand2);
                    this.infixExpressionStack.push("(" + c1 + " * " + c2 + ")");
                    break;
                case "/":
                    if (operand2 == 0) {
                        System.out.println("Cannot Divide by zero");
                        try {

                            throw new IllegalArgumentException("Cannot Divide by zero");
                        } catch (IllegalArgumentException e) {
                            e.getMessage();
                            System.exit(0);
                        }
                        // return;
                    }
                    this.calculatorStack.push(operand1 / operand2);
                    this.infixExpressionStack.push("(" + c1 + " / " + c2 + ")");
                    break;
                case "%":
                    this.calculatorStack.push(operand1 % operand2);
                    this.infixExpressionStack.push("(" + c1 + " % " + c2 + ")");
                    break;
                default:
                    try {
                        throw new NotValidOperatorException();
                    } catch (NotValidOperatorException e) {
                        System.out.println(e.getMessage());
                        System.exit(0);
                    }

            }

        }
        // Recursive call for the next element in the prefix expression
        processInfixExpressionCalculation(index - 1); // alternative to using for loop (elemnt)
    }

    /**
     * Prints the results (prefix expression, infix expression, and result).
     */
    private void printResults() {
        if (this.prefixExpressionStack.stackSize() != 0) {
            System.out.println("(Error in Prefix Expression) , can't create correct prefix expression");
            return;
        }

        // Print the prefix expression
        System.out.println("Prefix Expression: " + getPrefixExpression());
        if (this.calculatorStack.stackSize() != 0 || this.infixExpressionStack.stackSize() != 0) {
            try {
                throw new MissingOperator();
            } catch (MissingOperator e) {

                System.out.println(e.getMessage());

                System.exit(0);
            }
        }

        // Print the infix expression
        System.out.println("Infix Expression: " + getInfixExpression());
        // Print the result
        System.out.println("Result: " + this.result);
    }

    /**
     * Checks if a string is numeric.
     * 
     * @param str String to check.
     * @return True if the string is numeric, false otherwise.
     */
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Main method for testing the PrefixToInfix class.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        PrefixToInfix matching = new PrefixToInfix();
        matching.calcInfixExpressionResultFromXml("xmlFiles/file4.xml");
    }
}
