package trie;


import aStar.streetMap.Node;

import java.util.ArrayList;
import java.util.List;

public class Trie {

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(Node node) {

        String word = node.name();
        if(word == null || word.equals(""))
            return;

        TrieNode cur = root;
        char[] chars = word.toLowerCase().toCharArray();
        // add the string to the trie and clean it along the way
        for(char c : chars) {
            if(c >= 'a' && c <= 'z') {
                int idx = (c - 'a');
                if(cur.children[idx] == null)
                    cur.children[idx] = new TrieNode();
                cur = cur.children[idx];
            }
        }

        cur.addName(node);

    }

    // return all the Strings that have the specified prefix
    public List<Node> searchByPrefix(String prefix) {

        List<Node> ans = new ArrayList<>();

        TrieNode dfsStartPoint = findNode(prefix);
        findAllStrings(ans, dfsStartPoint);

        return ans;

    }

    public List<Node> searchByName(String name) {

        TrieNode tailNode = findNode(name);

        return tailNode.getNodes();

    }

    // return the node which will be used for search all the string by DFS
    private TrieNode findNode(String prefix) {

        TrieNode cur = root;
        for(char c : prefix.toLowerCase().toCharArray()) {
            if(c >= 'a' && c <= 'z') {
                int idx = (c - 'a');
                if(cur.children[idx] == null)
                    return null;
                cur = cur.children[idx];
            }
        }

        return cur;

    }

    private void findAllStrings(List<Node> ans, TrieNode root) {

        if(root == null)
            return;

        List<Node> nodes = root.getNodes();
        if(nodes != null) {
            for(Node n : nodes)
                ans.add(n);
        }

        for(TrieNode child : root.children)
            findAllStrings(ans, child);

    }

    class TrieNode {
        private TrieNode[] children;
        private List<Node> nodes;

        public TrieNode() {
            children = new TrieNode[26];
            nodes = null;
        }

        public List<Node> getNodes() {
            return nodes;
        }

        public void addName(Node n) {
            if(nodes == null)
                nodes = new ArrayList<>();

            nodes.add(n);
        }
    }

}
