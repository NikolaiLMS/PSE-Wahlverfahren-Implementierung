package edu.pse.beast.toolbox.valueContainer.cbmcValueContainers;
import java.util.List;

import org.w3c.dom.Element;

import edu.pse.beast.toolbox.valueContainer.ResultValue;

public interface CBMCResultValue extends ResultValue {
    public void setValue(Element element);
}