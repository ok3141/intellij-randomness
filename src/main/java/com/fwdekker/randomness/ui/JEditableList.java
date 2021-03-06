package com.fwdekker.randomness.ui;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * A {@link javax.swing.JList} in which each entry has a {@link javax.swing.JCheckBox} in front of it.
 *
 * @param <T> the entry type
 */
public final class JEditableList<T> extends JTable {
    private final DefaultTableModel model;
    private final List<EntryActivityChangeListener> entryActivityChangeListeners;


    /**
     * Constructs a new empty {@code JEditableList}.
     */
    public JEditableList() {
        super();

        model = new DefaultTableModel(0, 2);
        setModel(model);

        entryActivityChangeListeners = new ArrayList<>();
        model.addTableModelListener(event -> {
            if (event.getType() == TableModelEvent.UPDATE && event.getColumn() == 0) {
                entryActivityChangeListeners.forEach(listener -> listener.accept(event.getFirstRow()));
            }
        });

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setTableHeader(null);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent event) {
                super.componentResized(event);
                resizeColumns();
            }
        });
    }


    /**
     * Adds an entry to the list.
     * <p>
     * If the given entry is already in the list, nothing happens.
     *
     * @param entry the entry to add
     */
    public void addEntry(final T entry) {
        if (!hasEntry(entry)) {
            model.addRow(new Object[]{false, entry});
        }
    }

    /**
     * Removes all current entries, and adds the given entries.
     *
     * @param entries the entries to add
     */
    public void setEntries(final Collection<T> entries) {
        clear();
        entries.forEach(this::addEntry);
    }

    /**
     * Returns {@code true} iff. the given entry exists in the list.
     *
     * @param entry the entry to check for presence
     * @return {@code true} iff. the given entry exists in the list
     */
    private boolean hasEntry(final T entry) {
        return IntStream.range(0, getEntryCount())
                .anyMatch(row -> getEntry(row).equals(entry));
    }

    /**
     * Returns the entry in the given row.
     *
     * @param row the row to return the entry of
     * @return the entry in the given row
     */
    @SuppressWarnings("unchecked") // Type guaranteed by design
    public T getEntry(final int row) {
        return (T) model.getValueAt(row, 1);
    }

    /**
     * Returns the row number of the given entry.
     *
     * @param entry the entry to return the row number of
     * @return the row number of the given entry
     */
    private int getEntryRow(final T entry) {
        return IntStream.range(0, getEntryCount())
                .filter(row -> getEntry(row).equals(entry))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No row with entry `" + entry + "` found."));
    }

    /**
     * Returns a list of all entries.
     *
     * @return a list of all entries
     */
    public List<T> getEntries() {
        return IntStream.range(0, getEntryCount())
                .mapToObj(this::getEntry)
                .collect(Collectors.toList());
    }

    /**
     * Removes the given entry and its checkbox.
     *
     * @param entry the entry to remove
     */
    public void removeEntry(final T entry) {
        model.removeRow(getEntryRow(entry));
    }

    /**
     * Removes all entries.
     */
    public void clear() {
        IntStream.range(0, getEntryCount()).forEach(row -> model.removeRow(0));
    }

    /**
     * Returns the number of entries in the list.
     *
     * @return the number of entries in the list
     */
    public int getEntryCount() {
        return model.getRowCount();
    }


    /**
     * Returns all entries of which the checkbox is checked.
     *
     * @return all entries of which the checkbox is checked
     */
    public List<T> getActiveEntries() {
        return getEntries().stream()
                .filter(this::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Sets whether the given entry has its checkbox checked.
     *
     * @param entry    the entry to (un)check the checkbox of
     * @param selected {@code true} iff. the entry's checkbox should be checked
     */
    private void setActive(final T entry, final boolean selected) {
        setValueAt(selected, getEntryRow(entry), 0);
    }

    /**
     * Returns {@code true} iff. the given entry's checkbox is checked.
     *
     * @param entry the entry of which to return the checkbox's status
     * @return {@code true} iff. the given entry's checkbox is checked
     */
    public boolean isActive(final T entry) {
        return (Boolean) getValueAt(getEntryRow(entry), 0);
    }

    /**
     * Sets the activity of the given entry.
     *
     * @param entry    the entry to set the activity of
     * @param activity {@code true} iff. the entry's checkbox should be checked
     */
    public void setEntryActivity(final T entry, final boolean activity) {
        setValueAt(activity, getEntryRow(entry), 0);
    }

    /**
     * Checks the checkboxes of all given entries, and unchecks all other checkboxes.
     *
     * @param entries exactly those entries of which the checkboxes should be checked
     */
    public void setActiveEntries(final Collection<T> entries) {
        getEntries().forEach(entry -> setActive(entry, entries.contains(entry)));
    }

    /**
     * Adds a listener that is called when a row's activity is changed by the user.
     *
     * @param listener the listener to be added
     */
    public void addEntryActivityChangeListener(final EntryActivityChangeListener listener) {
        entryActivityChangeListeners.add(listener);
    }

    /**
     * Removes a listener that was called when a row's activity was changed by the user.
     *
     * @param listener the listener to be removed
     */
    public void removeEntryActivityChangeListener(final EntryActivityChangeListener listener) {
        entryActivityChangeListeners.remove(listener);
    }


    /**
     * Returns the entry that is currently selected by the user, if there is one.
     *
     * @return the entry that is currently selected by the user, if there is one
     */
    public Optional<T> getHighlightedEntry() {
        return Arrays.stream(getSelectedRows())
                .mapToObj(this::getEntry)
                .findFirst();
    }


    /**
     * Recalculates column widths.
     */
    private void resizeColumns() {
        final float[] columnWidthPercentages = {20.0f, 80.0f};

        final TableColumnModel columnModel = getColumnModel();
        for (int i = 0; i < model.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth((int) (columnWidthPercentages[i] * getWidth()));
        }
    }

    @Override
    public Class<?> getColumnClass(final int column) {
        switch (column) {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            default:
                throw new IllegalArgumentException("JEditableList only has two columns.");
        }
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        return column == 0;
    }


    /**
     * Listens to changes in the list's activity column.
     */
    @FunctionalInterface
    public interface EntryActivityChangeListener {
        /**
         * Indicates that the activity of a row has been changed.
         *
         * @param changedRow the index of the row of which the activity was changed
         */
        void accept(int changedRow);
    }
}
