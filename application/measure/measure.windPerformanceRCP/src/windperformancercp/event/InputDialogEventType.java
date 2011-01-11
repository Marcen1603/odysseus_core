package windperformancercp.event;

public enum InputDialogEventType implements IEventType {
	NewAttributeItem, DeleteAttributeItem, ChangeAttributeItem,
	NewSourceItem, DeleteSourceItem, ChangeSourceItem,
	RegisterDialog, DeregisterDialog
}
