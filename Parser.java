/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser

    Actualizado: agosto de 2021, Luis Cu
*/

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    // Puntero next que apunta al siguiente token
    private int next;
    // Stacks para evaluar en el momento
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    // LinkedList de tokens
    private LinkedList<Token> tokens;

    // Funcion que manda a llamar main para parsear la expresion
    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();

        // Recursive Descent Parser
        // Imprime si el input fue aceptado
        System.out.println("Aceptada? " + S());

        // Shunting Yard Algorithm
        // Imprime el resultado de operar el input
        // System.out.println("Resultado: " + this.operandos.peek());

        // Verifica si terminamos de consumir el input
        if(this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        if(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {
            
            // Codigo para el Shunting Yard Algorithm
            
            if (id == Token.NUMBER) {
				// Encontramos un numero
				// Debemos guardarlo en el stack de operandos
				operandos.push( this.tokens.get(this.next).getVal() );

			} else if (id == Token.SEMI) {
				// Encontramos un punto y coma
				// Debemos operar todo lo que quedo pendiente
				while (!this.operadores.empty()) {
					popOp();
				}
				
			} else {
				// Encontramos algun otro token, es decir un operador
				// Lo guardamos en el stack de operadores
				// Que pushOp haga el trabajo, no quiero hacerlo yo aqui
				pushOp( this.tokens.get(this.next) );
			}
			

            this.next++;
            return true;
        }
        return false;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        switch(op.getId()) {
        	case Token.PLUS:
                return 1;
            case Token.MINUS:
        		return 1;
        	case Token.MULT:
                return 2;
            case Token.DIV:
                return 2;
            case Token.MOD:
        		return 2;
            case Token.EXP:
                return 3;
            case Token.LPAREN:
                return 4;
            case Token.RPAREN:
                return 4;
        	default:
        		return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();

        if (op.equals(Token.PLUS)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("suma " + a + " + " + b);
        	this.operandos.push(a + b);
        }else if(op.equals(Token.MINUS)){
            double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("resta " + a + " - " + b);
        	this.operandos.push(a - b);
        }else if (op.equals(Token.MULT)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("mult " + a + " * " + b);
        	this.operandos.push(a * b);
        }else if (op.equals(Token.DIV)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("div " + a + " / " + b);
        	this.operandos.push(a / b);
        }else if (op.equals(Token.MOD)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("mod " + a + " % " + b);
        	this.operandos.push(a % b);
        }else if (op.equals(Token.EXP)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("exp " + a + " ^ " + b);
        	this.operandos.push(Math.pow(a, b));
        }
    }

    private void pushOp(Token op) {
        while(!this.operadores.empty() && pre(this.operadores.peek()) >= pre(op)){
            popOp(); 
        }
        this.operadores.push(op);
    }

    private boolean S() {
        return E() && term(Token.SEMI);
    }

    private boolean E() {
        return T() && Ep();
    }

    private boolean Ep(){
        if(next < tokens.size() && (tokens.get(next).getId() == Token.PLUS || tokens.get(next).getId() == Token.MINUS)){
            Token op = tokens.get(next);
            if(T()){
                pushOp(op);
                return Ep();
            }
            return false;
        }
        return true;
    }

    private boolean T(){
        return F() && Tp();
    }

    private boolean Tp(){
         if(next < tokens.size() && (tokens.get(next).getId() == Token.MULT || tokens.get(next).getId() == Token.DIV || tokens.get(next).getId() == Token.MOD || tokens.get(next).getId() == Token.EXP)){
            Token op = tokens.get(next);
            if(F()){
                pushOp(op);
                return Tp();
            }
            return false;
        }
        return true;
    }

    private boolean F(){
        if(term(Token.MINUS)){
            if(F()){
                double a = this.operandos.pop();
                this.operandos.push(-a);
                return true;
            }
            return false;
        }else if(term(Token.LPAREN)){
            boolean resultado = E();
            return resultado && term(Token.RPAREN);
        }else if(term(Token.NUMBER)){
            return true;
        }
        return false;
    }

}
