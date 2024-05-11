package ahlers.tree.path.parser

import ahlers.tree.path.term.Index
import ahlers.tree.path.term.Name
import ahlers.tree.path.term.Wildcard
import ahlers.tree.path.term.diffx.instances._
import ahlers.tree.path.term.scalacheck.instances._
import com.softwaremill.diffx.scalatest.DiffShouldMatcher.convertToAnyShouldMatcher
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks._
import parsley.Failure
import parsley.Success
import parsley.diffx.instances._

class TermSpec extends AnyWordSpec {

  "Index" should {
    val parser = term.index

    """accept positive or negative numeric""" in {
      forAll { index: Index =>
        parser.parse(index.toText).shouldMatchTo(Success(index))
      }
    }

    "reject else" in {
      forAll { input: String =>
        whenever(!input.matches("^[0-9]+$")) {
          parser.parse(input).shouldBe(a[Failure[_]])
        }
      }
    }
  }

  "Name" should {
    val parser = term.name

    """accept alpha-numeric""" in {
      forAll { name: Name =>
        parser.parse(name.toText).shouldMatchTo(Success(name))
      }
    }

    "reject else" in {
      forAll { input: String =>
        whenever(!input.matches("^[a-zA-Z0-9]+$")) {
          parser.parse(input).shouldBe(a[Failure[_]])
        }
      }
    }
  }

  "Wildcard" should {
    val parser = term.wildcard

    """accept "*"""" in {
      parser.parse("*").shouldMatchTo(Success(Wildcard))
    }

    "reject else" in {
      forAll { input: String =>
        whenever("*" != input) {
          parser.parse(input).shouldBe(a[Failure[_]])
        }
      }
    }
  }

}
