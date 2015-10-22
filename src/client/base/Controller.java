package client.base;

import shared.model.AbstractModelListener;
import shared.model.IModelListener;
import shared.model.ModelFacade;

/**
 * Base class for controllers
 */
public abstract class Controller extends AbstractModelListener implements IController, IModelListener
{
	
	private IView view;
	private ModelFacade modelFacade = ModelFacade.getInstance();
	
	protected Controller(IView view)
	{
		modelFacade.registerListener(this);
		setView(view);
	}
	
	private void setView(IView view)
	{
		this.view = view;
	}
	
	@Override
	public IView getView()
	{
		return this.view;
	}
	
}

