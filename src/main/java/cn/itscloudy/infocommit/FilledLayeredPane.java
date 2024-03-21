package cn.itscloudy.infocommit;

import com.intellij.uiDesigner.core.GridConstraints;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class FilledLayeredPane extends JLayeredPane {

    private final Layer[] layerPanels;

    public FilledLayeredPane(int layers) {
        assert layers > 0;
        setOpaque(false);
        layerPanels = new Layer[layers];
        GridConstraints gc = new GridConstraints();
        for (int i = 0; i < layers; i++) {
            Layer panel = createLayerPanel();
            add(panel, gc);
            setLayer(panel, i + 1);
            layerPanels[i] = panel;
        }
    }

    @NotNull
    private Layer createLayerPanel() {
        Layer panel = new Layer();
        panel.setBounds(0, 0, getWidth(), getHeight());
        return panel;
    }

    public JPanel getLayerPanel(int layer) {
        assert layer > 0 && layer <= layerPanels.length;
        return layerPanels[layer - 1];
    }

    public JPanel getTopLayer() {
        return layerPanels[layerPanels.length - 1];
    }

    @Override
    public Dimension getPreferredSize() {
        int maxWidth = -1;
        int maxHeight = -1;
        for (JPanel layerPanel : layerPanels) {
            Dimension preferredSize = layerPanel.getPreferredSize();
            if (preferredSize.width > maxWidth) {
                maxWidth = preferredSize.width;
            }
            if (preferredSize.height > maxHeight) {
                maxHeight = preferredSize.height;
            }
        }
        return new Dimension(maxWidth, maxHeight);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        for (JPanel layerPanel : layerPanels) {
            layerPanel.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void removeAll() {
    }

    @Override
    public void remove(Component comp) {
    }

    @Override
    public Component add(Component comp) {
        return comp;
    }

    public void revalidateAllLayers() {
        for (JPanel layerPanel : layerPanels) {
            layerPanel.revalidate();
            layerPanel.updateUI();
        }
    }

    public static class Layer extends JPanel {

        private Layer() {
            super.setOpaque(false);
        }

        @Override
        public void setOpaque(boolean isOpaque) {
            super.setOpaque(false);
        }

        @Override
        public boolean contains(int x, int y) {
            Component[] comps = getComponents();
            for (Component comp : comps) {
                if (comp.contains(x - comp.getX(), y - comp.getY())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void setBorder(Border border) {
            super.setBorder(border);
        }
    }
}
