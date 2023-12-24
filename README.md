# PrefixToInfix Converter and Calculator

This Java program converts prefix expressions to infix expressions and performs calculations based on XML input.

## Overview

The `PrefixToInfix` class provides functionality to convert prefix expressions to infix expressions and perform calculations based on XML input. It utilizes a stack-based approach to process XML elements and generate the corresponding infix expression and result.

## Usage

### Prerequisites

- Java installed on your machine.

### Running the Example

To test the `PrefixToInfix` class, you can use the provided `main` method:

```java
public static void main(String[] args) {
    PrefixToInfix converter = new PrefixToInfix();
    converter.calcInfixExpressionResultFromXml("xmlFiles/file4.xml");
}
```
Replace the file path in the `calcInfixExpressionResultFromXml` method with the path to your XML file.

## Features
- Conversion of prefix expressions to infix expressions.
- Calculation of results from XML input.
- Error handling for missing operators, missing operands, and invalid operators.

## XML Input Format

The XML file should have the following structure:

```xml
<expression>
    <operator value="*">
        <atom value="2"/>
        <atom value="3"/>
    </operator>
</expression>
```
Error Handling
- **MissingOperator:** Raised when an expected operator is missing.
- **MissingOperand:** Raised when an expected operand is missing.
- **NotValidOperatorException:** Raised when an invalid operator is encountered.
- **IllegalArgumentException:** Raised when attempting to divide by zero.

Acknowledgments
- Java SAX Parser
- Stack Data Structure in Java
- XML Document Object Model (DOM)
