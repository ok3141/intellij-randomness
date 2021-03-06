package com.fwdekker.randomness.integer;

import com.fwdekker.randomness.DataArrayInsertAction;
import com.fwdekker.randomness.DataGroupAction;
import com.fwdekker.randomness.DataInsertAction;
import com.fwdekker.randomness.SettingsAction;


/**
 * All actions related to inserting integers.
 */
public final class IntegerGroupAction extends DataGroupAction {
    @Override
    protected DataInsertAction getInsertAction() {
        return new IntegerInsertAction();
    }

    @Override
    protected DataArrayInsertAction getInsertArrayAction() {
        return new IntegerArrayInsertAction();
    }

    @Override
    protected SettingsAction getSettingsAction() {
        return new IntegerSettingsAction();
    }
}
