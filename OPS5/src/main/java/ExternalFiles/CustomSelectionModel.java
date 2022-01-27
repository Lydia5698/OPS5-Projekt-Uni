package ExternalFiles;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import jooq.tables.pojos.*;

/**
 * This class is the selection model for the combobox to fix the mistakes of the single selection model
 * @param <T>
 */
public class CustomSelectionModel<T> extends SingleSelectionModel<T> {

    private final ComboBox<T> comboBox;

    public CustomSelectionModel(ComboBox<T> comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public void select(T obj) {
        if (obj == null) {
            setSelectedIndex(-1);
            setSelectedItem(null);
            return;
        }

        final int itemCount = getItemCount();

        for (int i = 0; i < itemCount; i++) {
            final T value = getModelItem(i);
            if (value != null) {
                if (value instanceof OpsCodeSt) {
                    if (((OpsCodeSt) value).getOpsCode().equals(((OpsCodeSt) obj).getOpsCode())) {
                        select(i);
                    }
                } else if (value instanceof Operation) {
                    if (((Operation) value).getOpId().equals(((Operation) obj).getOpId())) {
                        select(i);
                    }
                } else if (value instanceof Icd10CodeSt) {
                    if (((Icd10CodeSt) value).getIcd10Code().equals(((Icd10CodeSt) obj).getIcd10Code())) {
                        select(i);
                    }
                } else if (value instanceof StationSt) {
                    if (((StationSt) value).getStation().equals(((StationSt) obj).getStation())) {
                        select(i);
                    }
                } else if (value instanceof Patient) {
                    if (((Patient) value).getPatId().equals(((Patient) obj).getPatId())) {
                        select(i);
                    }
                } else if (value instanceof DiagnosetypSt) {
                    if (((DiagnosetypSt) value).getDiagnosetyp().equals(((DiagnosetypSt) obj).getDiagnosetyp())) {
                        select(i);
                    }
                } else if (value instanceof FallTypSt) {
                    if (((FallTypSt) value).getFallTypId().equals(((FallTypSt) obj).getFallTypId())) {
                        select(i);
                    }
                } else if (value instanceof MedPersonal) {
                    if (((MedPersonal) value).getPersId().equals(((MedPersonal) obj).getPersId())) {
                        select(i);
                    }
                } else if (value instanceof Fall) {
                    if (((Fall) value).getFallId().equals(((Fall) obj).getFallId())) {
                        select(i);
                    }
                } else if (value instanceof OpTypSt) {
                    if (((OpTypSt) value).getOpTyp().equals(((OpTypSt) obj).getOpTyp())) {
                        select(i);
                    }
                } else if (value instanceof OpSaalSt) {
                    if (((OpSaalSt) value).getCode().equals(((OpSaalSt) obj).getCode())) {
                        select(i);
                    }
                } else if (value instanceof NarkoseSt) {
                    if (((NarkoseSt) value).getNarkose().equals(((NarkoseSt) obj).getNarkose())) {
                        select(i);
                    }
                } else if (value instanceof RolleSt) {
                    if (((RolleSt) value).getRolle().equals(((RolleSt) obj).getRolle())) {
                        select(i);
                    }
                }
                return;
            }

            // if we are here, we did not find the item in the entire data model.
            // Even still, we allow for this item to be set to the give object.
            // We expect that in concrete subclasses of this class we observe the
            // data model such that we check to see if the given item exists in it,
            // whilst SelectedIndex == -1 && SelectedItem != null.
            setSelectedItem(obj);

        }


    }

    @Override
    protected T getModelItem(int index) {
        final ObservableList<T> items = comboBox.getItems();
        if (items == null) return null;
        if (index < 0 || index >= items.size()) return null;
        return items.get(index);

    }

    @Override
    protected int getItemCount() {
        final ObservableList<T> items = comboBox.getItems();
        return items == null ? 0 : items.size();
    }

}
