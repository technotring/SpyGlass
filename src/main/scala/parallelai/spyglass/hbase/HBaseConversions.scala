package parallelai.spyglass.hbase

import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import com.twitter.scalding.Dsl._
import cascading.pipe.Pipe
import cascading.tuple.Fields
import com.twitter.scalding.RichPipe
import com.twitter.scalding.RichFields
import org.apache.hadoop.hbase.util.Bytes
import cascading.tuple.TupleEntry

class HBasePipeWrapper (pipe: Pipe) {
    def toBytesWritable(f: Fields): Pipe = {
	  asList(f)
        .foldLeft(pipe){ (p, f) => {
	      p.map(f.toString -> f.toString){ from: String =>
            Option(from).map(x => new ImmutableBytesWritable(Bytes.toBytes(x))).getOrElse(null)
          }}
      }
    }

	def fromBytesWritable(f: Fields): Pipe = {
	  asList(f)
	    .foldLeft(pipe) { (p, fld) => {
	      p.map(fld.toString -> fld.toString) { from: ImmutableBytesWritable =>
            Option(from).map(x => Bytes.toString(x.get)).getOrElse(null)
          }
        }}
    }
}

trait HBasePipeConversions {
  implicit def pipeWrapper(pipe: Pipe) = new HBasePipeWrapper(pipe) 
  implicit def richPipeWrapper(rp: RichPipe) = new HBasePipeWrapper(rp.pipe)
}


