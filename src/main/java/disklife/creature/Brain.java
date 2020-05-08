/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 *
 * @author
 * @version 1.0
 */
package disklife.creature;

import disklife.Vector2D;

import java.awt.*;

public class Brain extends Organ {

    public Brain(Creature creature, Vector2D pos, float r) {
        super(creature, pos, r);

    }

    public void draw(Graphics2D g) {
        g.setColor(Color.pink);
        super.draw(g);

    }

    void runTimeStep() {

    }


}