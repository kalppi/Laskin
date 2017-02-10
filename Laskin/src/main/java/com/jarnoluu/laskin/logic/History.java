package com.jarnoluu.laskin.logic;

import java.util.LinkedList;
import java.util.List;
import org.javatuples.Pair;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class History {
    private List<Pair<List<Token>, Double>> history = new LinkedList();
    
    public void add(List<Token> list, double val) {
        this.history.add(Pair.with(list, val));
    }
}
