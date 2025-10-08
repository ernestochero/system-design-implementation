package mini_project_01.api

import cats.effect.IO
import io.circe.generic.auto._
import mini_project_01.domain.Asset
import mini_project_01.repository.AssetRepository
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{EntityEncoder, HttpRoutes}

object AssetRoutes {
  def routes(repo: AssetRepository): HttpRoutes[IO] = {
    implicit val encoder: EntityEncoder[IO, List[Asset]] = jsonEncoderOf[IO, List[Asset]]
    implicit val singleEncoder: EntityEncoder[IO, Option[Asset]] = jsonEncoderOf[IO, Option[Asset]]

    HttpRoutes.of[IO] {
      case GET -> Root / "assets" =>
        repo.getAll.flatMap(Ok(_))
      case GET -> Root / "assets" / id =>
        repo.find(id).flatMap(Ok(_))
    }
  }
}
