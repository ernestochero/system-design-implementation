import munit.CatsEffectSuite
import org.http4s._
import org.http4s.implicits._
import org.http4s.circe._
import io.circe.Json
import cats.effect.IO
import cats.effect.Ref
import mini_project_01.api.AssetRoutes
import mini_project_01.domain.{Asset, AssetState}
import mini_project_01.repository.AssetRepository

class AssetRouteSpec extends CatsEffectSuite {
  val testAssets = Map(
    "1" -> Asset("1", "Asset 1", AssetState.Ready),
    "2" -> Asset("2", "Asset 2", AssetState.Pending)
  )
  def createRepo(ref: Ref[IO, Map[String, Asset]]): AssetRepository = new AssetRepository(ref) {
    override def getAll: IO[List[Asset]] = ref.get.map(_.values.toList)
    override def find(id: String): IO[Option[Asset]] = ref.get.map(_.get(id))
  }

  test("GET /assets should return all assets") {
    Ref.of[IO, Map[String, Asset]](testAssets).flatMap { ref =>
      val repo = createRepo(ref)
      val routes = AssetRoutes.routes(repo).orNotFound
      val request = Request[IO](Method.GET, uri"/assets")
      for {
        response <- routes.run(request)
        body <- response.as[Json]
      } yield {
        assertEquals(response.status, Status.Ok)
        assert(body.isArray)
        assertEquals(body.asArray.get.length, testAssets.size)
      }
    }
  }

  test("GET /assets/{id} should return asset by id") {
    Ref.of[IO, Map[String, Asset]](testAssets).flatMap { ref =>
      val repo = createRepo(ref)
      val routes = AssetRoutes.routes(repo).orNotFound
      val request = Request[IO](Method.GET, uri"/assets/1")
      for {
        response <- routes.run(request)
        body <- response.as[Json]
      } yield {
        assertEquals(response.status, Status.Ok)
        assertEquals(body.hcursor.downField("id").as[String].getOrElse(""), "1")
      }
    }
  }

  test("GET /assets/{id} should return None for missing asset") {
    Ref.of[IO, Map[String, Asset]](testAssets).flatMap { ref =>
      val repo = createRepo(ref)
      val routes = AssetRoutes.routes(repo).orNotFound
      val request = Request[IO](Method.GET, uri"/assets/999")
      for {
        response <- routes.run(request)
        body <- response.as[Json]
      } yield {
        assertEquals(response.status, Status.Ok)
        assert(body.isNull)
      }
    }
  }
}
