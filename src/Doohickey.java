public class Doohickey
{
    private String _name;
    private int _value;
    private boolean _deliver;
    private ConditionEnum _condition;
    private short _setupPlan;
    private boolean _isNew;

    public Doohickey()
    {
        _name = "";
        _value = 0;
        _deliver = false;
        _condition = ConditionEnum.NEW;
        _setupPlan = 1;
        _isNew = false;
    }

    public Doohickey(String name, int value, boolean deliver, ConditionEnum condition, short plan, boolean isNew)
    {
        _name = name;
        _value = value;
        _deliver = deliver;
        _condition = condition;
        _setupPlan = plan;
        _isNew = isNew;
    }

    public Doohickey(String token)
    {
        String[] rawValues = token.split(",");
        rawValues[4] = rawValues[4].stripTrailing(); // attempt to remove trailing newline

        _name = rawValues[0];
        _value = Integer.valueOf(rawValues[1]);
        _deliver = Boolean.valueOf(rawValues[2]);
        _condition = ConditionEnum.valueOf(rawValues[3]);
        _setupPlan = Short.valueOf(rawValues[4]);
        _isNew = false; // not going to be new if being read from a CSV file.
    }

    // DEVELOPER'S NOTE: This is part of an example app, so no input validation
    public String name() {return _name; }
    public Doohickey name(String newName) { _name = newName; return this; }

    public int value() { return _value; }
    public Doohickey value(int newValue) { _value = newValue; return this; }

    public ConditionEnum condition() { return _condition; }
    public Doohickey condition(ConditionEnum newCond) { _condition = newCond; return this; }

    public short plan() { return _setupPlan; }
    public Doohickey plan(short newPlan) { _setupPlan = newPlan; return this; }

    public boolean deliver() { return _deliver; }
    public Doohickey deliver(boolean newValue) { _deliver = newValue; return this; }

    public boolean getNew() { return _isNew; }
    public Doohickey setnew(boolean val) { _isNew = val; return this; }

    public String WriteCsv()
    {
        return _name + "," + String.valueOf(_value) + "," + String.valueOf(_deliver) + "," + _condition.name() + "," + String.valueOf(_setupPlan) + "\n";
    }
}
