//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.ui.bgs;

import pythagoras.f.IDimension;

import tripleplay.ui.Background;

/**
 * A background that displays a solid color.
 */
public class SolidBackground extends Background
{
    /**
     * Creates a solid background with the specified color and insets.
     */
    public SolidBackground (int color, float top, float right, float bottom, float left) {
        super(top, right, bottom, left);
        _color = color;
    }

    @Override protected Instance instantiate (IDimension size) {
        return new LayerInstance(createSolidLayer(_color, size.width(), size.height()));
    }

    protected int _color;
}
