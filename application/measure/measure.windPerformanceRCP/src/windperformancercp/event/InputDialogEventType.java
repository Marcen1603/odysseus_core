package windperformancercp.event;

public enum InputDialogEventType implements IEventType {
	NewAttributeItem, DeleteAttributeItem, ChangeAttributeItem,
	NewSourceItem, DeleteSourceItem, ChangeSourceItem,
	NewPerformanceItem, DeletePerformanceItem, ChangePerformanceItem,
	RegisterDialog, DeregisterDialog
}
