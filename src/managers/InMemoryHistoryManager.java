package managers;

import tasks.Task;
import utilities.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY = 10;
    private final CustomLinkedList history = new CustomLinkedList();
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();


    // добавить задачу в историю просмотров
    @Override
    public void add(Task task) {
        history.linkLast(task);
        if (historyMap.size() > MAX_HISTORY) {
            int headId = history.head.data.getId();
            history.removeNode(history.head);
            historyMap.remove(headId);
        }
    }

    //  удалить задачу из истории просмотров
    @Override
    public void remove(int id) {
        Node<Task> node = historyMap.get(id);
        history.removeNode(node);
        historyMap.remove(id);
    }

    // получить историю просмотров
    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    class CustomLinkedList {
        private Node<Task> head;
        private Node<Task> tail;

        // добавить задачу в историю просмотров
        public void linkLast(Task task) {
            int id = task.getId();

            if (historyMap.containsKey(id)) {
                removeNode(historyMap.get(id));
            }

            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;

            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            historyMap.put(id, newNode);
        }

        // получить все задачи из истории просмотров
        public List<Task> getTasks() {
            Node<Task> curHead = head;
            List<Task> elementsInList = new ArrayList<>();

            while (curHead != null) {
                elementsInList.add(curHead.data);
                curHead = curHead.next;
            }
            return elementsInList;
        }

        // удалить узел из истории просмотров
        private void removeNode(Node<Task> node) {
            if (node == null) {
                return;
            }
            if (node.prev == null) {
                Node<Task> nextNode = node.next;
                nextNode.prev = null;
                head = nextNode;
            } else if (node.next == null) {
                Node<Task> prevNode = node.prev;
                prevNode.next = null;
                tail = prevNode;
            } else {
                Node<Task> nextNode = node.next;
                Node<Task> prevNode = node.prev;
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
        }
    }
}