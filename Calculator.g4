grammar Calculator;
 
expression: multiplyingExpression ((PLUS | MINUS) multiplyingExpression)*;
multiplyingExpression: integralExpression ((MULT | DIV) integralExpression)*;
integralExpression: MINUS powExpression| powExpression;
powExpression: INT (POW (INT | LPAREN multiplyingExpression RPAREN))*;

INT: [0-9]+ ;
PLUS: '+' ;
MINUS: '-' ;
MULT: '*';
DIV: '/';
POW: '^';
LPAREN: '(';
RPAREN: ')';
INTEGRAL: 'cal';
WS : [ \t\r\n]+ -> skip ;
