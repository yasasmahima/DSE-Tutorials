import org.apache.pig.data.DataBag;
import org.apache.pig.data.BagFactory;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import java.io.IOException;

/**
 * Convert a string into positional ranked data bag based on a delimiter.
 */
public class RankRowVal extends EvalFunc<DataBag> {

    private final String delimiter;

    public RankRowVal(final String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public DataBag exec(final Tuple row) throws IOException {

        final String rowVal = (String) row.get(0);
        final int sign = (Integer) row.get(1);

        final String[] rowSplits = rowVal.split(this.delimiter);
        final DataBag bag = BagFactory.getInstance().newDefaultBag();

        for (int x = 0; x <= rowSplits.length - 1 ; x++) {

            final int y = (sign == -1) ? x + 1  : x ;

            final Tuple tuple = TupleFactory.getInstance().newTuple(2);
            tuple.set(0, ((x == 0) ? y : y * sign));
            tuple.set(1, rowSplits[x]);
            bag.add(tuple);
        }

        return (bag);

    }
}