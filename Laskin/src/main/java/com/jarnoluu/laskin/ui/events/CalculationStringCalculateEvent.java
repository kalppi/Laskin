package com.jarnoluu.laskin.ui.events;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public interface CalculationStringCalculateEvent {
    void onCalculate(Double val, Double old);
}
