package javacalass;
import java.util.*;


class Node {
    String type;
    Node left;
    Node right;
    Object value;

    public Node(String type, Node left, Node right, Object value) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.value = value;
    }

    public boolean isOperator() {
        return type.equals("operator");
    }
}

// Rule Engine class
class RuleEngine {


    public static Node createRule(String ruleString) {
        return parseRule(ruleString);
    }

    // Function to parse rule string into AST.
    private static Node parseRule(String ruleString) {
        Node ageCondition = new Node("operand", null, null, "age > 30");
        Node departmentCondition = new Node("operand", null, null, "department = 'Sales'");
        return new Node("operator", ageCondition, departmentCondition, "AND");
    }

    // Combine multiple rules into a single AST
    public static Node combineRules(List<String> rules) {
        List<Node> ruleNodes = new ArrayList<>();
        for (String rule : rules) {
            ruleNodes.add(createRule(rule));
        }


        Node combined = ruleNodes.get(0);
        for (int i = 1; i < ruleNodes.size(); i++) {
            combined = new Node("operator", combined, ruleNodes.get(i), "AND");
        }
        return combined;
    }

    // Evaluate a rule AST against provided user data
    public static boolean evaluateRule(Node node, Map<String, Object> data) {
        if (node.isOperator()) {
            boolean left = evaluateRule(node.left, data);
            boolean right = evaluateRule(node.right, data);
            if (node.value.equals("AND")) {
                return left && right;
            } else if (node.value.equals("OR")) {
                return left || right;
            }
        } else {
            return evaluateCondition(node.value.toString(), data);
        }
        return false;
    }


    private static boolean evaluateCondition(String condition, Map<String, Object> data) {

        String[] parts = condition.split(" ");
        String attribute = parts[0];
        String operator = parts[1];
        String value = parts[2];

        Object dataValue = data.get(attribute);

        if (operator.equals(">")) {
            return Integer.parseInt(dataValue.toString()) > Integer.parseInt(value);
        } else if (operator.equals("<")) {
            return Integer.parseInt(dataValue.toString()) < Integer.parseInt(value);
        } else if (operator.equals("=")) {
            return dataValue.toString().equals(value);
        }

        return false;
    }

    // Test cases
    public static void main(String[] args) {
        // Rule 1
        String rule1 = "((age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing')) AND (salary > 50000 OR experience > 5)";
        Node rule1AST = createRule(rule1);

        // Rule 2
        String rule2 = "((age > 30 AND department = 'Marketing')) AND (salary > 20000 OR experience > 5)";
        Node rule2AST = createRule(rule2);

        Node combinedAST = combineRules(Arrays.asList(rule1, rule2));

        Map<String, Object> userData = new HashMap<>();
        userData.put("age", 35);
        userData.put("department", "Sales");
        userData.put("salary", 60000);
        userData.put("experience", 3);

        boolean result = evaluateRule(combinedAST, userData);
        System.out.println("Evaluation Result: " + result);
    }
}

