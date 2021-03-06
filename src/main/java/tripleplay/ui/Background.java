//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.ui;

import playn.core.CanvasLayer;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.PlayN;

import pythagoras.f.Dimension;
import pythagoras.f.FloatMath;
import pythagoras.f.IDimension;

import tripleplay.ui.bgs.SolidBackground;

/**
 * A background is responsible for rendering a border and a fill. It is used in conjunction with
 * groups and buttons and any other elements that need a background.
 */
public abstract class Background
{
    /** The (highest) depth at which background layers are rendered. May range from (-11, 10]. */
    public static final float BACKGROUND_DEPTH = -10f;

    /**
     * Creates a solid background of the specified color, with no insets.
     */
    public static Background solid (int color) {
        return new SolidBackground(color, 0, 0, 0, 0);
    }

    /**
     * Creates a solid background of the specified color and (uniform) insets.
     */
    public static Background solid (int color, float inset) {
        return new SolidBackground(color, inset, inset, inset, inset);
    }

    /**
     * Creates a solid background of the specified color and insets.
     */
    public static Background solid (int color, float top, float right, float bottom, float left) {
        return new SolidBackground(color, top, right, bottom, left);
    }

    /** The insets of this background. */
    public final float top, right, bottom, left;

    /** Returns this background's adjustment to an element's width. */
    public float width () {
        return left + right;
    }

    /** Returns this background's adjustment to an element's height. */
    public float height () {
        return top + bottom;
    }

    /**
     * Adds this background's insests to the supplied dimensions. Returns {@code size} for chaning.
     */
    public Dimension addInsets (Dimension size) {
        size.width += width();
        size.height += height();
        return size;
    }

    /**
     * Instantiates this background using the supplied widget size. The supplied size should
     * include the insets defined for this backround.
     */
    protected abstract Instance instantiate (IDimension size);

    protected Background (float top, float right, float bottom, float left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    protected static Layer createSolidLayer (int color, float width, float height) {
        // TODO: rewrite this as an active-rendered layer when PlayN supports such things
        int cwidth = FloatMath.iceil(width), cheight = FloatMath.iceil(height);
        CanvasLayer canvas = PlayN.graphics().createCanvasLayer(cwidth, cheight);
        canvas.canvas().setFillColor(color).fillRect(0, 0, width, height);
        return canvas;
    }

    protected static Layer createTiledLayer (Image image, float width, float height) {
        ImageLayer layer = PlayN.graphics().createImageLayer(image);
        layer.setRepeatX(true);
        layer.setRepeatY(true);
        return layer;
    }

    /** An instantiation of a particular background template. Backgrounds are configured as a style
     * property; elements instantiate them at specific dimensions when they are actually used.*/
    protected static abstract class Instance {
        /** Adds this background's layers to the specified group. */
        public abstract void addTo (GroupLayer parent);

        /** Disposes of this background instance when it is no longer valid/needed. */
        public abstract void destroy ();
    }

    protected static class LayerInstance extends Instance {
        public LayerInstance (Layer... layers) {
            _layers = layers;
            for (Layer layer : _layers) {
                layer.setDepth(BACKGROUND_DEPTH);
            }
        }
        @Override public void addTo (GroupLayer parent) {
            for (Layer layer : _layers) {
                parent.add(layer);
            }
        }
        @Override public void destroy () {
            for (Layer layer : _layers) {
                layer.destroy();
            }
        }
        protected Layer[] _layers;
    }
}
