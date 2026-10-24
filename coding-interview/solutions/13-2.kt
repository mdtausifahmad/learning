import java.util.*

class Node(val value: Int) {
    val neighbors: MutableList<Node> = mutableListOf()
}
fun cloneGraph(root: Node?): Node? {
    if (root == null) return null
    val copies = IdentityHashMap<Node, Node>()
    val queue = ArrayDeque<Node>()
    copies[root] = Node(root.value)
    queue.add(root)
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        for (neighbor in current.neighbors) {
            if (!copies.containsKey(neighbor)) {
                copies[neighbor] = Node(neighbor.value)
                queue.add(neighbor)
            }
            copies.getValue(current).neighbors.add(copies.getValue(neighbor))
        }
    }
    return copies[root]
}
