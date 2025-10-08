package mini_project_01

import cats.effect.{IO, IOApp}
import mini_project_01.api.AssetRoutes
import mini_project_01.repository.AssetRepository
import mini_project_01.services.EventProcessor
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._

object Main extends IOApp.Simple {
  def run: IO[Unit] = for {
    repo <- AssetRepository.create
    _ <- EventProcessor.stream(repo).compile.drain.start // run in background

    httpApp = AssetRoutes.routes(repo).orNotFound

    _ <- BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
  } yield ()
}
