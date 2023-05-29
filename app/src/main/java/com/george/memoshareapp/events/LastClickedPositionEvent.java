package com.george.memoshareapp.events;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.events
 * @className: LastClickedPositionEvent
 * @author: George
 * @description: TODO
 * @date: 2023/5/20 18:02
 * @version: 1.0
 */
public class LastClickedPositionEvent {
    private int position;

    public LastClickedPositionEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}

