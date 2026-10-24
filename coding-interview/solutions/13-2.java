import java.util.*;

class Solution13P2 {
static final class Node {
    final int value;
    final List<Node> neighbors = new ArrayList<>();
    Node(int value) { this.value = value; }
}
static Node cloneGraph(Node root) {
    if (root == null) return null;
    IdentityHashMap<Node, Node> copies = new IdentityHashMap<>();
    ArrayDeque<Node> queue = new ArrayDeque<>();
    copies.put(root, new Node(root.value));
    queue.add(root);
    while (!queue.isEmpty()) {
        Node current = queue.remove();
        for (Node neighbor : current.neighbors) {
            if (!copies.containsKey(neighbor)) {
                copies.put(neighbor, new Node(neighbor.value));
                queue.add(neighbor);
            }
            copies.get(current).neighbors.add(copies.get(neighbor));
        }
    }
    return copies.get(root);
}
}
