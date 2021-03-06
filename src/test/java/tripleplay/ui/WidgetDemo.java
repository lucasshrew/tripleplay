//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.ui;

import playn.core.Game;
import playn.core.PlayN;
import static playn.core.PlayN.graphics;
import playn.java.JavaPlatform;

import react.UnitSlot;

/**
 * A test app for demoing the UI widgets.
 */
public class WidgetDemo implements Game
{
    public static void main (String[] args) {
        JavaPlatform platform = JavaPlatform.register();
        platform.assetManager().setPathPrefix("src/test/resources");
        PlayN.run(new WidgetDemo());
    }

    public interface Page {
        String name ();
        Group createInterface ();
    }

    @Override // from interface Game
    public void init () {
        _iface = new Interface(null);
        PlayN.pointer().setListener(_iface.plistener);
        PlayN.keyboard().setListener(_iface.klistener);

        // define our root stylesheet
        Stylesheet rootSheet = Stylesheet.builder().
            add(Button.class, Styles.none().
                add(Style.BACKGROUND.is(Background.solid(0xFFFFFFFF, 5))).
                addSelected(Style.BACKGROUND.is(Background.solid(0xFFCCCCCC, 6, 4, 4, 6)))).
            add(Label.class, Style.HALIGN.left, Style.VALIGN.top).
            create();

        // create our demo interface
        final Root root = _iface.createRoot(AxisLayout.vertical().offStretch(), rootSheet,
                graphics().rootLayer()).
            setSize(graphics().width(), graphics().height()).
            addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF, 5)), Style.VALIGN.top);

        Group buttons = new Group(AxisLayout.horizontal(), Style.HALIGN.left);
        root.add(buttons);

        Page[] pages = { new MiscPage(), new TablePage() };
        for (final Page page : pages) {
            Button tab = new Button(page.name());
            buttons.add(tab);
            tab.clicked().connect(new UnitSlot() {
                public void onEmit () {
                    if (root.childCount() > 1) root.destroyAt(1);
                    root.add(page.createInterface());
                }
            });
        }
        root.add(pages[0].createInterface());
    }

    @Override // from interface Game
    public void update (float delta) {
        _iface.update(delta);
    }

    @Override // from interface Game
    public void paint (float alpha) {
        _iface.paint(alpha);
    }

    @Override // from interface Game
    public int updateRate () {
        return 30;
    }

    protected Interface _iface;
}
